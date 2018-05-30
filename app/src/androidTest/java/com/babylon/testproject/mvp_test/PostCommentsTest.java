package com.babylon.testproject.mvp_test;

import android.support.test.runner.AndroidJUnit4;

import com.babylon.testproject.BaseTest;
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.HttpException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Antonis Latas
 */
@RunWith(AndroidJUnit4.class)
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
        initMocks(this);
        repository = spy(new CommentsRepository(apiService, postForAssertion.postId, storage, schedulersProvider));
        postDetailsPresenter = spy(new PostDetailsPresenter(screen, repository));
    }

    @Test
    public void testPostCommentsFromDatabase() {


        doReturn(getRandromPostComments()).when(storage).loadPostComments(postForAssertion.postId);
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();
        verify(screen).showCommentNumbers(postForAssertion.numOfComments);
        verify(screen, never()).onCommentsError();
        verify(storage, never()).addPostComments(any(PostComments.class));

    }


    @Test
    public void testPostCommentsFromRemote() {

        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        doReturn(randomCommentsResponse()).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        int otherPostsComments = (int) (totalComments - postForAssertion.numOfComments);

        verify(storage, atMost(otherPostsComments + 1))
                .addPostComments(any(PostComments.class));
        verify(storage, atLeast(2)).addPostComments(any(PostComments.class));

        verify(repository).fetchFromRemote(any(ResultListener.class));
        verify(screen).showCommentNumbers(postForAssertion.numOfComments);
        verify(screen, never()).onCommentsError();
    }


    @Test
    public void testPostNoCommentsWithServerFailure() {

        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        doReturn(Observable.error(exception)).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        verify(storage, never()).addPostComments(any(PostComments.class));
        verify(repository).fetchFromRemote(any(ResultListener.class));
        verify(screen, times(0)).showCommentNumbers(anyInt());
        verify(screen).onCommentsError();
    }

    @Test
    public void testPostNoCommentsWithSuccess_EmptyComments() {

        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        doReturn(Observable.just(Collections.emptyList())).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        ArgumentCaptor<PostComments> captor = ArgumentCaptor.forClass(PostComments.class);

        verify(storage).addPostComments(captor.capture());
        Assert.assertEquals(postForAssertion.postId, captor.getValue().postId);
        Assert.assertEquals(0, captor.getValue().numOfComments);

        verify(repository).fetchFromRemote(any(ResultListener.class));
        verify(screen).showCommentNumbers(0);
        verify(screen, never()).onCommentsError();
    }


    @Test
    public void testPostNoCommentsWithSuccess_NoCommentsForTheSpecificPost() {

        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadPostComments(postForAssertion.postId);
        doReturn(randomCommentsResponseWithoutTestPost()).when(apiService).getComments();
        postDetailsPresenter.loadComments();
        testScheduler.triggerActions();

        verify(storage, times(3)).addPostComments(any(PostComments.class));
        verify(repository).fetchFromRemote(any(ResultListener.class));
        verify(screen).showCommentNumbers(0);
        verify(screen, never()).onCommentsError();
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
        System.out.println("rrrr " + commentResp.size());

        for (CommentResp a : commentResp) {
            if (a.postId == 10)
                System.out.println(a.postId);
        }
        return Observable.just(commentResp);
    }


    private Single<PostComments> getRandromPostComments() {
        return Single.just(postForAssertion);
    }


}
