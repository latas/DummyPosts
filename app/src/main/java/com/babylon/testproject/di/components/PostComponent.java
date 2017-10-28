package com.babylon.testproject.di.components;


import com.babylon.testproject.di.modules.PostModule;
import com.babylon.testproject.di.scopes.ActivityScope;
import com.babylon.testproject.ui.PostDetailsActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PostModule.class, dependencies = AppComponent.class)
public interface PostComponent {

    void injectPostActivity(PostDetailsActivity activity);

}
