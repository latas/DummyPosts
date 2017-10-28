package com.babylon.testproject.mvp_test;

import android.support.test.runner.AndroidJUnit4;

import com.babylon.testproject.data.interactors.Repository;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.presenters.PostDetailsPresenter;
import com.babylon.testproject.ui.DetailsScreen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by Antonis Latas
 */
@RunWith(AndroidJUnit4.class)
public class PostDetailsPresenterTest {

    @Mock
    DetailsScreen screen;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostComments() {
        final PostComments postComments = new PostComments(5, 82);

        Repository<PostComments> repo = listener -> {
            listener.start();
            listener.success(postComments);
        };

        PostDetailsPresenter postDetailsPresenter = new PostDetailsPresenter(screen, repo);

        postDetailsPresenter.loadComments();

        verify(screen).showCommentNumbers(82);
        verify(screen, never()).onCommentsError();
    }

    @Test
    public void testFailureOnComments() {

        Repository<PostComments> repo = listener -> {
            listener.start();
            listener.failure();
        };
        PostDetailsPresenter postDetailsPresenter = new PostDetailsPresenter(screen, repo);
        postDetailsPresenter.loadComments();
        verify(screen).onCommentsError();
        verify(screen,never()).showCommentNumbers(anyLong());
    }

}
