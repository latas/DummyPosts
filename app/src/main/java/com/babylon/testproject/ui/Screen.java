package com.babylon.testproject.ui;

public interface Screen {
    void showLoading();

    void onError(String error);

    void hideLoading();
}
