package com.babylon.testproject.di.modules;


import android.arch.persistence.room.Room;
import android.content.Context;

import com.babylon.testproject.data.database.AppDb;
import com.babylon.testproject.data.database.DatabaseConfig;
import com.babylon.testproject.data.database.PostCommentsDao;
import com.babylon.testproject.data.database.UserDao;
import com.babylon.testproject.di.scopes.AppScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.class)
public class DatabaseModule {
    private static final String DATABASE = "database";

    @Provides
    @AppScope
    @Named(DATABASE)
    String databaseName(Context context) {
        return DatabaseConfig.DATABASE_NAME;
    }

    @Provides
    @AppScope
    AppDb appDbDao(Context context, @Named(DATABASE) String databaseName) {
        return Room.databaseBuilder(context, AppDb.class, databaseName).build();
    }

    @Provides
    @AppScope
    UserDao userDao(AppDb appDb) {
        return appDb.userDao();
    }

    @Provides
    @AppScope
    PostCommentsDao postCommentsDao(AppDb appDb) {
        return appDb.postCommentsDao();
    }

}

