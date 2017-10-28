package com.babylon.testproject.data.models;

public class Comment {

    final long commentId;
    public final long postId;
    public final String body;

    public Comment(long commentId, long postId, String body) {
        this.commentId = commentId;
        this.postId = postId;
        this.body = body;
    }
}
