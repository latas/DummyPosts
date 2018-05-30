package com.babylon.testproject.mvptest;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.HttpException;

/**
 * Created by Antonis Latas
 */
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
        userPresenter = Mockito.spy(new UserPresenter(new UsersRepository(storage, apiService, schedulersProvider).user(userForAssertion.id)));
    }

    @Test
    public void testUserFromDatabase() {
        Mockito.doReturn(Single.just(userForAssertion)).when(storage).loadUser(userForAssertion.id);
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(displayer).showInfoForUser(userCaptor.capture());
        Assert.assertEquals(userForAssertion, userCaptor.getValue());
        Mockito.verify(displayer, Mockito.never()).onUserLoadFailed(userForAssertion.id);
    }


    @Test
    public void testUserFromRemote() {

        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        Mockito.doReturn(getRandomUsersIncludingUserForAssertion()).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);

        testScheduler.triggerActions();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(displayer).showInfoForUser(userCaptor.capture());
        Mockito.verify(storage, Mockito.times(randomUsersCount)).addUser(Matchers.any(User.class));
        Assert.assertEquals(userForAssertion, userCaptor.getValue());
        Mockito.verify(displayer, Mockito.never()).onUserLoadFailed(userForAssertion.id);
    }


    @Test
    public void testUserNotFound_ServerFailure() {
        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        Mockito.doReturn(Observable.error(exception)).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        Mockito.verify(displayer, Mockito.times(0)).showInfoForUser(Matchers.any(User.class));
        Mockito.verify(displayer).onUserLoadFailed(userForAssertion.id);
    }


    @Test
    public void testUserNotFound_ServerSuccess() {
        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        Mockito.doReturn(getRandomUsersWithoutUserForAssertion()).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        Mockito.verify(storage, Mockito.times(randomUsersCount)).addUser(Matchers.any(User.class));
        Mockito.verify(displayer, Mockito.never()).showInfoForUser(Matchers.any(User.class));
        Mockito.verify(displayer).onUserLoadFailed(userForAssertion.id);
    }

    @Test
    public void testNoUsersFound() {
        Mockito.doReturn(Single.error(new Throwable("No records found"))).when(storage).loadUser(userForAssertion.id);
        Mockito.doReturn(Observable.just(Collections.emptyList())).when(apiService).getUsers();
        userPresenter.loadUser(userForAssertion.id, displayer);
        testScheduler.triggerActions();
        Mockito.verify(storage, Mockito.never()).addUser(Matchers.any(User.class));
        Mockito.verify(displayer, Mockito.never()).showInfoForUser(Matchers.any(User.class));
        Mockito.verify(displayer).onUserLoadFailed(userForAssertion.id);
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
