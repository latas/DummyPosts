package com.babylon.testproject.data.responses;

import com.babylon.testproject.data.models.Post;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostResp {

    @Expose
    @SerializedName("id")
    public final long id;

    @Expose
    @SerializedName("userId")
    public final long userId;

    @Expose
    @SerializedName("body")
    public final String bdy;

    @Expose
    @SerializedName("title")
    public final String title;


    public PostResp(long id, long userId, String bdy, String title) {
        this.id = id;
        this.userId = userId;
        this.bdy = bdy;
        this.title = title;
    }

    public Post map() {
        return new Post(id, title, bdy, userId);
    }

}
