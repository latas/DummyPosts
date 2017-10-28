package com.babylon.testproject.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.babylon.testproject.data.database.DatabaseConfig;

/**
 * Created by Antonis Latas
 */
@Entity(tableName = DatabaseConfig.POSTS_COMMENTS_TABLE_NAME)
public class PostComments {
    @PrimaryKey
    public final long postId;
    public final long numOfComments;


    public PostComments(long postId, long numOfComments) {
        this.postId = postId;
        this.numOfComments = numOfComments;
    }
}
