package com.babylon.testproject.presenters;


import com.babylon.testproject.data.interactors.UsersRepository;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.data.models.User;
import com.babylon.testproject.ui.UserDisplayer;

import javax.inject.Inject;

public class UserPresenter {

    final UsersRepository usersRepository;

    @Inject
    public UserPresenter(UsersRepository repository) {
        this.usersRepository = repository;
    }


    public void loadUser(final long userId, final UserDisplayer displayer) {
        usersRepository.user(userId).data(new ResultListener<User>() {
            @Override
            public void start() {
                displayer.showUserLoading();
            }

            @Override
            public void success(User user) {
                displayer.showInfoForUser(user);
            }

            @Override
            public void failure() {
                displayer.onUserLoadFailed(userId);
            }
        });
    }

}
