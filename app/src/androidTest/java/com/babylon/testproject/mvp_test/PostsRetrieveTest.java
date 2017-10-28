package com.babylon.testproject.mvp_test;

import android.support.test.runner.AndroidJUnit4;

import com.babylon.testproject.BaseTest;
import com.babylon.testproject.data.interactors.PostsRepository;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.responses.PostResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.presenters.PostsPresenter;
import com.babylon.testproject.ui.ListScreen;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.HttpException;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(AndroidJUnit4.class)
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
        initMocks(this);
    }


    @Test
    public void checkPosts() {

        PostsRepository repository = new PostsRepository(apiService, schedulersProvider);
        doReturn(getRandomPostsRespObservable()).when(apiService).getPosts();

        PostsPresenter presenter = spy(new PostsPresenter(listScreen, repository));

        presenter.retrievePosts();
        testScheduler.triggerActions();

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        verify(listScreen).showLoading();
        verify(presenter).success(anyListOf(Post.class));
        verify(listScreen).hideLoading();
        verify(listScreen).showPosts(captor.capture());
        verify(listScreen, Mockito.never()).onError(anyString());
        Assert.assertEquals(randomPostsNumber, captor.getValue().size());
    }

    @Test
    public void checkNoPosts() {
        PostsRepository repository = new PostsRepository(apiService,schedulersProvider);
        doReturn(Observable.just(Collections.emptyList())).when(apiService).getPosts();


        PostsPresenter presenter = new PostsPresenter(listScreen, repository);

        presenter.retrievePosts();
        testScheduler.triggerActions();

        verify(listScreen).showLoading();
        verify(listScreen).hideLoading();
        verify(listScreen, Mockito.never()).showPosts(Matchers.anyListOf(Post.class));

    }

    @Test
    public void checkPostsFailure() {

        PostsRepository repository = new PostsRepository(apiService,schedulersProvider);

        doReturn(Observable.error(exception))
                .when(apiService).getPosts();

        PostsPresenter presenter = new PostsPresenter(listScreen, repository);

        presenter.retrievePosts();
        testScheduler.triggerActions();

        verify(listScreen).showLoading();

        verify(listScreen).hideLoading();
        verify(listScreen).onError("Error retrieving posts");
        verify(listScreen, Mockito.never()).showPosts(Matchers.anyListOf(Post.class));
    }


    private Observable<List<PostResp>> getRandomPostsRespObservable() {
        SecureRandom random = new SecureRandom();
        List<PostResp> postResps = new ArrayList<>();
        for (int i = 0; i < randomPostsNumber; i++)
            postResps.add(new PostResp(random.nextLong(), random.nextLong(), "title" + i, "randomPostText" + i));
        return Observable.just(postResps);
    }

}
