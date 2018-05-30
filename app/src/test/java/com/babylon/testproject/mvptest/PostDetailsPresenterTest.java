package com.babylon.testproject.mvptest;

import com.babylon.testproject.data.interactors.Repository;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.presenters.PostDetailsPresenter;
import com.babylon.testproject.ui.DetailsScreen;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Created by Antonis Latas
 */
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

        Mockito.verify(screen).showCommentNumbers(82);
        Mockito.verify(screen, Mockito.never()).onCommentsError();
    }

    @Test
    public void testFailureOnComments() {

        Repository<PostComments> repo = listener -> {
            listener.start();
            listener.failure();
        };
        PostDetailsPresenter postDetailsPresenter = new PostDetailsPresenter(screen, repo);
        postDetailsPresenter.loadComments();
        Mockito.verify(screen).onCommentsError();
        Mockito.verify(screen, Mockito.never()).showCommentNumbers(Matchers.anyLong());
    }

}
