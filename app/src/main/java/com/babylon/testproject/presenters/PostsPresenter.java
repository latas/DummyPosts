package com.babylon.testproject.presenters;

import com.babylon.testproject.data.interactors.Repository;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.ui.ListScreen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostsPresenter implements ResultListener<List<Post>> {
    final ListScreen screen;
    final Repository<List<Post>> postsRepository;
    List<Post> posts = new ArrayList<>();


    @Inject
    public PostsPresenter(ListScreen screen, Repository<List<Post>> repository) {
        this.screen = screen;
        this.postsRepository = repository;
        screen.setTitle("Posts");
    }

    public void retrievePosts() {
        postsRepository.data(this);
    }

    @Override
    public void start() {
        screen.showLoading();
    }

    @Override
    public void success(List<Post> posts) {
        this.posts = posts;
        screen.hideLoading();
        if (posts.size() > 0)
            screen.showPosts(posts);
        else screen.onError("No posts found");

    }

    @Override
    public void failure() {
        screen.hideLoading();
        screen.onError("Error retrieving posts");
    }

    public void onListItemClicked(int position) {
        screen.navigateToDetails(posts.get(position));
    }
}
