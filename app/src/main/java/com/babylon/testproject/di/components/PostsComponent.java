package com.babylon.testproject.di.components;

import com.babylon.testproject.di.modules.PostsModule;
import com.babylon.testproject.di.scopes.ActivityScope;
import com.babylon.testproject.ui.PostsActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PostsModule.class, dependencies = AppComponent.class)
public interface PostsComponent {
    void injectPostsActivity(PostsActivity activity);
}