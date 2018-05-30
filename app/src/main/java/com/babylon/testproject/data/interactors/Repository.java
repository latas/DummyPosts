package com.babylon.testproject.data.interactors;

import com.babylon.testproject.data.models.ResultListener;

public interface Repository<T> {
    void data(ResultListener<T> listener);

}

