package com.babylon.testproject.di.modules;

import android.app.Activity;
import android.view.LayoutInflater;

import com.babylon.testproject.scheduler.SchedulerProvider;
import com.babylon.testproject.data.interactors.PostsRepository;
import com.babylon.testproject.data.interactors.Repository;
import com.babylon.testproject.data.models.Post;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.di.scopes.ActivityScope;
import com.babylon.testproject.ui.ListScreen;
import com.babylon.testproject.ui.listeners.RecyclerItemClickListener;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PostsModule {

    final ListScreen screen;
    final Activity activity;
    final RecyclerItemClickListener recyclerItemClickListener;

    public PostsModule(Activity activity, ListScreen screen, RecyclerItemClickListener recyclerItemClickListener) {
        this.screen = screen;
        this.activity = activity;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @Provides
    @ActivityScope
    public LayoutInflater inflater() {
        return activity.getLayoutInflater();
    }


    @Provides
    @ActivityScope
    public RecyclerItemClickListener recyclerItemClickListener() {
        return recyclerItemClickListener;
    }

    @Provides
    @ActivityScope
    public Repository<List<Post>> repository(ApiService service, SchedulerProvider provider) {
        return new PostsRepository(service, provider);
    }

    @Provides
    @ActivityScope
    public ListScreen screen() {
        return screen;
    }
}
