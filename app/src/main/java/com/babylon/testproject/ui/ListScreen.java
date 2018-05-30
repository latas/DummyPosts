package com.babylon.testproject.ui;

import com.babylon.testproject.data.models.Post;

import java.util.List;



public interface ListScreen extends ActivityScreen {
    void showPosts(List<Post> posts);


    void navigateToDetails(Post post);
}
