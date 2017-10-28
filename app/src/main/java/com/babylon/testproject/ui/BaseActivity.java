package com.babylon.testproject.ui;

import android.support.v7.app.AppCompatActivity;

import com.babylon.testproject.BabylonApp;
import com.babylon.testproject.di.components.AppComponent;


public class BaseActivity extends AppCompatActivity {
    protected AppComponent appComponent() {
        return ((BabylonApp) getApplication()).getApplicationComponent();

    }
}
