package com.babylon.testproject.data.rest;

import com.babylon.testproject.data.responses.CommentResp;
import com.babylon.testproject.data.responses.PostResp;
import com.babylon.testproject.data.responses.UserResp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface ApiService {
    @GET("posts")
    Observable<List<PostResp>> getPosts();


    @GET("users")
    Observable<List<UserResp>> getUsers();

    @GET("comments")
    Observable<List<CommentResp>> getComments();
}
