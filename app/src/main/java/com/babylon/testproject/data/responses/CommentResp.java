package com.babylon.testproject.data.responses;

import com.babylon.testproject.data.models.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentResp {
    @Expose
    @SerializedName("id")
    public final long commentId;

    @Expose
    @SerializedName("postId")
    public final long postId;


    @Expose
    @SerializedName("body")
    public final String body;


    public CommentResp(long commentId, long postId, String body) {
        this.postId = postId;
        this.commentId = commentId;
        this.body = body;
    }

    public Comment map() {
        return new Comment(commentId, postId, body);
    }
}
