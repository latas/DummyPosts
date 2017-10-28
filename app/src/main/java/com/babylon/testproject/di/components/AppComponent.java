package com.babylon.testproject.di.components;

import android.content.Context;

import com.babylon.testproject.scheduler.BaseSchedulerProvider;
import com.babylon.testproject.data.local.PostCommentsLocalStorage;
import com.babylon.testproject.data.local.UsersLocalStorage;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.di.modules.ApplicationModule;
import com.babylon.testproject.di.modules.DatabaseModule;
import com.babylon.testproject.di.modules.PicassoModule;
import com.babylon.testproject.di.modules.ServiceModule;
import com.babylon.testproject.di.scopes.AppScope;
import com.squareup.picasso.Picasso;

import dagger.Component;

/**
 * Created by Antonis Latas
 */

@AppScope
@Component(modules = {ServiceModule.class, PicassoModule.class, ApplicationModule.class, DatabaseModule.class})
public interface AppComponent {

    Picasso getPicasso();

    ApiService getPostsService();

    Context context();

    UsersLocalStorage storage();

    PostCommentsLocalStorage postCommentsStorage();

    BaseSchedulerProvider schedulerProvider();


}