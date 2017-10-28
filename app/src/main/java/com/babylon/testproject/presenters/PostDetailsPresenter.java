package com.babylon.testproject.presenters;

import com.babylon.testproject.data.interactors.Repository;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.ui.DetailsScreen;

import javax.inject.Inject;

public class PostDetailsPresenter implements ResultListener<PostComments> {
    final DetailsScreen screen;
    final Repository<PostComments> commentsRepository;

    @Inject
    public PostDetailsPresenter(DetailsScreen screen, Repository<PostComments> commentRepository) {
        this.commentsRepository = commentRepository;
        this.screen = screen;
        init();
    }

    private void init() {
        screen.showBackButton();
        screen.setTitle("Post Details");
    }

    public void loadComments() {
        commentsRepository.data(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void success(PostComments postComments) {
        screen.showCommentNumbers(postComments.numOfComments);
    }

    @Override
    public void failure() {
        screen.onCommentsError();

    }
}
