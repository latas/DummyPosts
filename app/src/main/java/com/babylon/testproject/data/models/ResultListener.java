package com.babylon.testproject.data.models;

public interface ResultListener<T> {

    void start();

    void success(T t);

    void failure();
}
