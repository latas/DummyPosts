package com.babylon.testproject.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.data.models.User;

/**
 * Created by Antonis Latas
 */
@Database(entities = {User.class, PostComments.class}, version = 1)
public abstract class AppDb extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract PostCommentsDao postCommentsDao();
}
