package com.babylon.testproject.mvp_test;

import android.support.test.runner.AndroidJUnit4;

import com.babylon.testproject.BaseTest;
import com.babylon.testproject.data.interactors.UsersRepository;
import com.babylon.testproject.data.local.UsersLocalStorage;
import com.babylon.testproject.data.models.User;
import com.babylon.testproject.data.responses.UserResp;
import com.babylon.testproject.data.rest.ApiService;
import com.babylon.testproject.presenters.UserPresenter;
import com.babylon.testproject.ui.UserDisplayer;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.HttpException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Antonis Latas
 */
@RunWith(AndroidJUnit4.class)
public class UsersTest extends BaseTest {


    @Mock
    UserDisplayer displayer;
    @Mock
    UsersLocalStorage storage;
    @Mock
    ApiService apiService;
    @Mock
    HttpException exception;
    final User userForAssertion = new User(15, "Lakis", "lakis@lakis.com");
    UserPresenter userPresenter;
    final int randomUsersCount = 10;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userPresenter = spy(new UserPresenter(new UsersRepository(storage, apiService, schedulersProvider).user(userForAssertion.id)));
    }

    @Test
    public void testUserFromDatabase() {
        doReturn(Single.just(userForAssertion)).when(storage).loadUser(userForAssertion.id);
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(displayer).showInfoForUser(userCaptor.capture());
        Assert.assertEquals(userForAssertion, userCaptor.getValue());
        verify(displayer, never()).onUserLoadFailed(userForAssertion.id);
    }


    @Test
    public void testUserFromRemote() {

        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        doReturn(getRandomUsersIncludingUserForAssertion()).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);

        testScheduler.triggerActions();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(displayer).showInfoForUser(userCaptor.capture());
        verify(storage, times(randomUsersCount)).addUser(any(User.class));
        Assert.assertEquals(userForAssertion, userCaptor.getValue());
        verify(displayer, never()).onUserLoadFailed(userForAssertion.id);
    }


    @Test
    public void testUserNotFound_ServerFailure() {
        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        doReturn(Observable.error(exception)).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        verify(displayer, times(0)).showInfoForUser(any(User.class));
        verify(displayer).onUserLoadFailed(userForAssertion.id);
    }


    @Test
    public void testUserNotFound_ServerSuccess() {
        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        doReturn(getRandomUsersWithoutUserForAssertion()).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        verify(storage, times(randomUsersCount)).addUser(any(User.class));
        verify(displayer, never()).showInfoForUser(any(User.class));
        verify(displayer).onUserLoadFailed(userForAssertion.id);
    }

    @Test
    public void testNoUsersFound() {
        doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        doReturn(Observable.just(Collections.emptyList())).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        verify(storage, never()).addUser(any(User.class));
        verify(displayer, never()).showInfoForUser(any(User.class));
        verify(displayer).onUserLoadFailed(userForAssertion.id);
    }

    private Observable<List<UserResp>> getRandomUsersIncludingUserForAssertion() {
        List<UserResp> randomUsers = new ArrayList<>();
        randomUsers.add(new UserResp(userForAssertion.id, userForAssertion.userName, userForAssertion.mail, "NameRandom"));
        for (int i = 1; i < randomUsersCount; i++) {
            randomUsers.add(new UserResp(userForAssertion.id + i, "lalalla " + i, "lakis" + i + "@" + i + "lakis.com", "NameRandom"));
        }
        return Observable.just(randomUsers);
    }


    private Object getRandomUsersWithoutUserForAssertion() {
        List<UserResp> randomUsers = new ArrayList<>();
        for (int i = 0; i < randomUsersCount; i++) {
            randomUsers.add(new UserResp(userForAssertion.id + 1 + i, "lalalla " + i, "lakis" + i + "@" + i + "lakis.com", "NameRandom"));
        }
        return Observable.just(randomUsers);
    }


}
