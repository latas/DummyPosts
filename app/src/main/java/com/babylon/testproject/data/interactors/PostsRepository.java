package com.babylon.testproject.data.interactors;

import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.data.responses.PostResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.scheduler.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PostsRepository implements Repository<List<Post>> {

    final ApiService apiService;
    final BaseSchedulerProvider provider;


    @Inject
    public PostsRepository(ApiService service, BaseSchedulerProvider provider) {
        this.apiService = service;
        this.provider = provider;
    }

    @Override
    public void data(final ResultListener<List<Post>> listener) {
        listener.start();

        apiService.getPosts().subscribeOn(provider.io()).map(postResps -> {
            List<Post> posts = new ArrayList<>();
            for (PostResp resp : postResps)
                posts.add(resp.map());
            return posts;
        }).observeOn(provider.ui()).subscribe(
                posts -> listener.success(posts), throwable -> listener.failure());
    }
}
