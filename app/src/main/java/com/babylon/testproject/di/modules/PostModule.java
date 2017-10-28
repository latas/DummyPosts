package com.babylon.testproject.di.modules;

import com.babylon.testproject.scheduler.SchedulerProvider;
import com.babylon.testproject.data.interactors.CommentsRepository;
import com.babylon.testproject.data.interactors.Repository;
import com.babylon.testproject.data.local.PostCommentsLocalStorage;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.di.scopes.ActivityScope;
import com.babylon.testproject.ui.DetailsScreen;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PostModule {

    final DetailsScreen screen;
    final long postId;

    public PostModule(DetailsScreen screen, long postId) {
        this.screen = screen;
        this.postId = postId;
    }


    @Provides
    @ActivityScope
    @Named("postId")
    public long postId() {
        return postId;
    }

    @Provides
    @ActivityScope
    public DetailsScreen screen() {
        return screen;
    }

    @Provides
    @ActivityScope
    public Repository<PostComments> repository(ApiService apiService, @Named("postId") long postId, PostCommentsLocalStorage storage, SchedulerProvider provider) {
        return new CommentsRepository(apiService, postId, storage, provider);
    }
}
