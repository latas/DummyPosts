package com.babylon.testproject.ui;

import com.babylon.testproject.data.models.User;

public interface UserDisplayer {
    void showInfoForUser(User user);

    void onUserLoadFailed(long userId);

    void showUserLoading();
}
