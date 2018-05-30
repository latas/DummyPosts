package com.babylon.testproject.data.interactors;

import com.babylon.testproject.data.local.PostCommentsLocalStorage;
import com.babylon.testproject.data.models.Comment;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.data.responses.CommentResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.scheduler.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class CommentsRepository implements Repository<PostComments> {

    final PostCommentsLocalStorage storage;
    final long postId;
    final ApiService service;
    final BaseSchedulerProvider provider;

    @Inject
    public CommentsRepository(ApiService service, @Named("postId") long postId, PostCommentsLocalStorage storage, BaseSchedulerProvider provider) {
        this.service = service;
        this.postId = postId;
        this.storage = storage;
        this.provider = provider;
    }

    @Override
    public void data(final ResultListener<PostComments> listener) {
        storage.loadPostComments(postId).subscribeOn(provider.io())
                .observeOn(provider.ui()).subscribe(comments -> listener.success(comments), throwable -> fetchFromRemote(listener));
    }


    public void fetchFromRemote(final ResultListener<PostComments> listener) {

        service.getComments().subscribeOn(provider.io())
                .map(commentsResp -> {
                    Map<Long, Long> postsComments = new HashMap<>();
                    postsComments.put(postId, 0L);
                    for (CommentResp commentResp : commentsResp) {
                        Comment comment = commentResp.map();
                        if (postsComments.containsKey(comment.postId)) {
                            postsComments.put(comment.postId, postsComments.get(comment.postId) + 1);
                        } else
                            postsComments.put(comment.postId, 1L);

                    }
                    return postsComments;
                })
                .flatMapIterable(map -> {
                    List<PostComments> list = new ArrayList<>();
                    for (Long postId : map.keySet()) {
                        PostComments postComments = new PostComments(postId, map.get(postId));
                        list.add(postComments);
                        storage.addPostComments(postComments);
                    }
                    return list;
                })
                .filter(comments -> comments.postId == postId)
                .observeOn(provider.ui()).subscribe(postComments -> listener.success(postComments), throwable -> listener.failure());
    }
}
