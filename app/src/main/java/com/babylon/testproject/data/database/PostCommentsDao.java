package com.babylon.testproject.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.babylon.testproject.data.models.PostComments;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Antonis Latas
 */
@Dao
public interface PostCommentsDao {
    @Query("SELECT * FROM " + DatabaseConfig.POSTS_COMMENTS_TABLE_NAME + " WHERE postId == :postId")
    Single<PostComments> load(long postId);

    @Query("SELECT * FROM " + DatabaseConfig.POSTS_COMMENTS_TABLE_NAME)
    Single<List<PostComments>> loadAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostComments postComments);

    @Query("DELETE FROM " + DatabaseConfig.POSTS_COMMENTS_TABLE_NAME)
    void deleteAll();
}
