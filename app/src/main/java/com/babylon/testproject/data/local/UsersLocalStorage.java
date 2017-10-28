package com.babylon.testproject.data.local;

import com.babylon.testproject.data.database.UserDao;
import com.babylon.testproject.data.models.User;

import javax.inject.Inject;

import io.reactivex.Single;


public class UsersLocalStorage {

    private final UserDao userDao;

    @Inject
    public UsersLocalStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    public Single<User> loadUser(long userId) {
        return userDao.loadUserId(userId);
    }


    public void addUser(User user) {
        userDao.insert(user);
    }

    public void clearData() {
        userDao.deleteAll();
    }

}

