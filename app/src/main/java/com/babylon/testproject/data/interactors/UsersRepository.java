package com.babylon.testproject.data.interactors;

import com.babylon.testproject.data.local.UsersLocalStorage;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.data.models.User;
import com.babylon.testproject.data.responses.UserResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.scheduler.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;


public class UsersRepository implements Repository<User> {

    final ApiService apiService;
    final UsersLocalStorage storage;
    private long mUserId = -Long.MAX_VALUE;
    final BaseSchedulerProvider provider;

    @Inject
    public UsersRepository(UsersLocalStorage storage, ApiService apiService, BaseSchedulerProvider provider) {
        this.storage = storage;
        this.apiService = apiService;
        this.provider = provider;
    }

    public UsersRepository user(long userId) {
        this.mUserId = userId;
        return this;
    }

    @Override
    public void data(final ResultListener<User> listener) {
        listener.start();
        if (mUserId == -Long.MAX_VALUE)
            throw new IllegalStateException("No user provided for search");

        final long userId = mUserId;

        storage.loadUser(userId).subscribeOn(provider.io())
                .observeOn(provider.ui()).subscribe(user -> listener.success(user), throwable -> fetchFromRemote(listener, userId));

    }

    private void fetchFromRemote(final ResultListener<User> listener, final long userId) {

        apiService.getUsers().subscribeOn(provider.io()).flatMapIterable(userResps -> {
            List<User> users = new ArrayList<>();
            for (UserResp u : userResps) {
                users.add(u.map());
                storage.addUser(u.map());
            }
            return users;
        }).filter(user -> user.id == userId).toList().observeOn(provider.ui()).subscribe(users -> {
            if (users.isEmpty()) listener.failure();
            else
                listener.success(users.get(0));
        }, throwable -> listener.failure());
    }
}
