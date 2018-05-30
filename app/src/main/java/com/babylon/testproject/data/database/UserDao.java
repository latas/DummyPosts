package com.babylon.testproject.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.babylon.testproject.data.models.User;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Antonis Latas on 10/25/2017
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM " + DatabaseConfig.USER_TABLE_NAME + " WHERE id == :userId")
    Single<User> loadUserId(long userId);

    @Query("SELECT * FROM " + DatabaseConfig.USER_TABLE_NAME)
    Single<List<User>> loadAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("DELETE FROM " + DatabaseConfig.USER_TABLE_NAME)
    void deleteAll();
}



