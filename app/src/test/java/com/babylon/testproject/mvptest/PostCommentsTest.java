package com.babylon.testproject.mvptest;


import com.babylon.testproject.data.interactors.CommentsRepository;
import com.babylon.testproject.data.local.PostCommentsLocalStorage;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.data.responses.CommentResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.presenters.PostDetailsPresenter;
import com.babylon.testproject.ui.DetailsScreen;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.HttpException;

/**
 * Created by Antonis Latas
 */
public class PostCommentsTest extends BaseTest {

    @Mock
    DetailsScreen screen;
    @Mock
    ApiService apiService;
    @Mock
    PostCommentsLocalStorage storage;

    final int totalComments = 20;
    final PostComments postForAssertion = new PostComments(10, 7);

    @Mock
    HttpException exception;
    CommentsRepository repository;
    PostDetailsPresenter postDetailsPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repository = Mockito.spy(new CommentsRepository(apiService, postForAssertion.postId, storage, schedulersProvider));
        postDetailsPresenter = Mockito.spy(new PostDetailsPresenter(screen, repository));
    }

    @Test
    public void testPostCommentsFromDatabase() {


        Mockito.doReturn(getRandromPostComments()).when(storage).loadPostComments(postForAssertion.postId);
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();
        Mockito.verify(screen).showCommentNumbers(postForAssertion.numOfComments);
        Mockito.verify(screen, Mockito.never()).onCommentsError();
        Mockito.verify(storage, Mockito.never()).addPostComments(Matchers.any(PostComments.class));

    }


    @Test
    public void testPostCommentsFromRemote() {

        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        Mockito.doReturn(randomCommentsResponse()).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        int otherPostsComments = (int) (totalComments - postForAssertion.numOfComments);

        Mockito.verify(storage, Mockito.atMost(otherPostsComments + 1))
                .addPostComments(Matchers.any(PostComments.class));
        Mockito.verify(storage, Mockito.atLeast(2)).addPostComments(Matchers.any(PostComments.class));

        Mockito.verify(repository).fetchFromRemote(Matchers.any(ResultListener.class));
        Mockito.verify(screen).showCommentNumbers(postForAssertion.numOfComments);
        Mockito.verify(screen, Mockito.never()).onCommentsError();
    }


    @Test
    public void testPostNoCommentsWithServerFailure() {

        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        Mockito.doReturn(Observable.error(exception)).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        Mockito.verify(storage, Mockito.never()).addPostComments(Matchers.any(PostComments.class));
        Mockito.verify(repository).fetchFromRemote(Matchers.any(ResultListener.class));
        Mockito.verify(screen, Mockito.times(0)).showCommentNumbers(Matchers.anyInt());
        Mockito.verify(screen).onCommentsError();
    }

    @Test
    public void testPostNoCommentsWithSuccess_EmptyComments() {

        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        Mockito.doReturn(Observable.just(Collections.emptyList())).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        ArgumentCaptor<PostComments> captor = ArgumentCaptor.forClass(PostComments.class);

        Mockito.verify(storage).addPostComments(captor.capture());
        Assert.assertEquals(postForAssertion.postId, captor.getValue().postId);
        Assert.assertEquals(0, captor.getValue().numOfComments);

        Mockito.verify(repository).fetchFromRemote(Matchers.any(ResultListener.class));
        Mockito.verify(screen).showCommentNumbers(0);
        Mockito.verify(screen, Mockito.never()).onCommentsError();
    }


    @Test
    public void testPostNoCommentsWithSuccess_NoCommentsForTheSpecificPost() {

        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        Mockito.doReturn(randomCommentsResponseWithoutTestPost()).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        Mockito.verify(storage, Mockito.times(3)).addPostComments(Matchers.any(PostComments.class));
        Mockito.verify(repository).fetchFromRemote(Matchers.any(ResultListener.class));
        Mockito.verify(screen).showCommentNumbers(0);
        Mockito.verify(screen, Mockito.never()).onCommentsError();
    }

    private Object randomCommentsResponseWithoutTestPost() {
        SecureRandom random = new SecureRandom();

        List<CommentResp> commentResp = new ArrayList<>();

        commentResp.add(new CommentResp(random.nextLong(), postForAssertion.postId + 1, "lalalalala"));
        commentResp.add(new CommentResp(random.nextLong(), postForAssertion.postId + 2, "lalalalala2"));
        return Observable.just(commentResp);
    }


    private Observable<List<CommentResp>> randomCommentsResponse() {
        SecureRandom random = new SecureRandom();

        List<CommentResp> commentResp = new ArrayList<>();
        for (int i = 0; i < totalComments; i++) {
            if (i % 3 == 0) {
                commentResp.add(new CommentResp(random.nextInt(), postForAssertion.postId, "lalallala " + i));
            } else
                commentResp.add(new CommentResp(random.nextLong(), Math.abs(random.nextLong()) + 1 +
                        postForAssertion.postId/* just to ensure that is different*/, "comment_body " + i));
        }
        return Observable.just(commentResp);
    }


    private Single<PostComments> getRandromPostComments() {
        return Single.just(postForAssertion);
    }


}
