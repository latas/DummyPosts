package com.babylon.testproject.instrumented;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import com.babylon.testproject.BaseTest;
import com.babylon.testproject.data.database.AppDb;
import com.babylon.testproject.data.interactors.UsersRepository;
import com.babylon.testproject.data.local.UsersLocalStorage;
import com.babylon.testproject.data.models.ResultListener;
import com.babylon.testproject.data.models.User;
import com.babylon.testproject.data.responses.UserResp;
import com.babylon.testproject.data.rest.ApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;

import io.reactivex.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class RetrieveUserTest extends BaseTest {

    AppDb appDb;
    @Mock
    ApiService service;

    @Mock
    ResultListener<User> resultListener;

    @Before
    public void initDb() throws Exception {
        initMocks(this);
        appDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                AppDb.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        appDb.close();
    }


    final User user = new User(15, "lakis", "lakis@lakis.com");


    @Test
    public void getUserRemotelyAndStoreToDb() {

        appDb.userDao().deleteAll();
        appDb.userDao().loadAll().test().assertValue(users -> users.isEmpty());

        UsersLocalStorage storage = spy(new UsersLocalStorage(appDb.userDao()));

        UsersRepository repository = new UsersRepository(storage, service, schedulersProvider).user(user.id);

        doReturn(Observable.just(Arrays.asList(new UserResp(user.id, user.userName, user.mail, "LakisName")))).when(service).getUsers();
        repository.data(resultListener);
        testScheduler.triggerActions();
        verify(storage).addUser(any(User.class));
        verify(resultListener).success(any(User.class));

        appDb.userDao().loadUserId(user.id).test().assertValue(retrievedUser -> retrievedUser.id == user.id);
    }


}
