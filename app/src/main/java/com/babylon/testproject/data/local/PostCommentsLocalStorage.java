package com.babylon.testproject.data.local;

import com.babylon.testproject.data.database.PostCommentsDao;
import com.babylon.testproject.data.models.PostComments;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by Antonis Latas
 */

public class PostCommentsLocalStorage {


    private final PostCommentsDao dao;

    @Inject
    PostCommentsLocalStorage(PostCommentsDao dao) {
        this.dao = dao;

    }

    public Single<PostComments> loadPostComments(long postId) {
        return dao.load(postId);
    }


    public void addPostComments(PostComments postComments) {
        dao.insert(postComments);
    }

    public void clearData() {
        dao.deleteAll();
    }
}
