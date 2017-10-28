package com.babylon.testproject.di.modules;

import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.di.scopes.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module(includes = {NetworkModule.class})

public class ServiceModule {
    @Provides
    @AppScope
    public ApiService service(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }


}

