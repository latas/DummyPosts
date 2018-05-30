package com.babylon.testproject.mvptest;

import com.babylon.testproject.data.interactors.PostsRepository;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.responses.PostResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.presenters.PostsPresenter;
import com.babylon.testproject.ui.ListScreen;

import org.junit.Assert;
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
import retrofit2.HttpException;


public class PostsRetrieveTest extends BaseTest {
    final int randomPostsNumber = 10;

    @Mock
    ListScreen listScreen;
    @Mock
    ApiService apiService;
    @Mock
    HttpException exception;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void checkPosts() {

        PostsRepository repository = new PostsRepository(apiService, schedulersProvider);
        Mockito.doReturn(getRandomPostsRespObservable()).when(apiService).getPosts();

        PostsPresenter presenter = Mockito.spy(new PostsPresenter(listScreen, repository));

        presenter.retrievePosts();
        testScheduler.triggerActions();

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        Mockito.verify(listScreen).showLoading();
        Mockito.verify(presenter).success(Matchers.anyListOf(Post.class));
        Mockito.verify(listScreen).hideLoading();
        Mockito.verify(listScreen).showPosts(captor.capture());
        Mockito.verify(listScreen, Mockito.never()).onError(Matchers.anyString());
        Assert.assertEquals(randomPostsNumber, captor.getValue().size());
    }

    @Test
    public void checkNoPosts() {
        PostsRepository repository = new PostsRepository(apiService,schedulersProvider);
        Mockito.doReturn(Observable.just(Collections.emptyList())).when(apiService).getPosts();


        PostsPresenter presenter = new PostsPresenter(listScreen, repository);

        presenter.retrievePosts();
        testScheduler.triggerActions();

        Mockito.verify(listScreen).showLoading();
        Mockito.verify(listScreen).hideLoading();
        Mockito.verify(listScreen, Mockito.never()).showPosts(Matchers.anyListOf(Post.class));

    }

    @Test
    public void checkPostsFailure() {

        PostsRepository repository = new PostsRepository(apiService,schedulersProvider);

        Mockito.doReturn(Observable.error(exception))
                .when(apiService).getPosts();

        PostsPresenter presenter = new PostsPresenter(listScreen, repository);

        presenter.retrievePosts();
        testScheduler.triggerActions();

        Mockito.verify(listScreen).showLoading();

        Mockito.verify(listScreen).hideLoading();
        Mockito.verify(listScreen).onError("Error retrieving posts");
        Mockito.verify(listScreen, Mockito.never()).showPosts(Matchers.anyListOf(Post.class));
    }


    private Observable<List<PostResp>> getRandomPostsRespObservable() {
        SecureRandom random = new SecureRandom();
        List<PostResp> postResps = new ArrayList<>();
        for (int i = 0; i < randomPostsNumber; i++)
            postResps.add(new PostResp(random.nextLong(), random.nextLong(), "title" + i, "randomPostText" + i));
        return Observable.just(postResps);
    }

}
