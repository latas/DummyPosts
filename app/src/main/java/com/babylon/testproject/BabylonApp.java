package com.babylon.testproject;

import android.app.Application;

import com.babylon.testproject.di.components.AppComponent;
import com.babylon.testproject.di.components.DaggerAppComponent;
import com.babylon.testproject.di.modules.ApplicationModule;


public class BabylonApp extends Application {
    private AppComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent =
                DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public AppComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
