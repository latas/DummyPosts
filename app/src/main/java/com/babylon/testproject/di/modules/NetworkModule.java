package com.babylon.testproject.di.modules;

import android.content.Context;

import com.babylon.testproject.R;
import com.babylon.testproject.di.scopes.AppScope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = ApplicationModule.class)
public class NetworkModule {


    @Provides
    @AppScope
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    @AppScope
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @AppScope
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson, Context context) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(context.getString(R.string.api_base_url))
                .build();
    }
}
