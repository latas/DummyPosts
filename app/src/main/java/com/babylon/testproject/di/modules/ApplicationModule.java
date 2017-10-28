package com.babylon.testproject.di.modules;

import android.content.Context;

import com.babylon.testproject.scheduler.BaseSchedulerProvider;
import com.babylon.testproject.scheduler.SchedulerProvider;
import com.babylon.testproject.di.scopes.AppScope;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {
    final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @AppScope
    @Provides
    public Context context() {
        return context;
    }


    @AppScope
    @Provides
    BaseSchedulerProvider providerSchedulerProvider(SchedulerProvider provider) {
        return provider;
    }

}
