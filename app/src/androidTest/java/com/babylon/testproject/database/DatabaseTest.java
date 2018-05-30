package com.babylon.testproject.database;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.babylon.testproject.data.database.AppDb;
import com.babylon.testproject.data.models.PostComments;
import com.babylon.testproject.data.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Antonis Latas
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    AppDb appDb;

    @Before
    public void initDb() throws Exception {
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

    @Test
    public void insertAndGetUser() {

        final User randomUser = randomUser();
        appDb.userDao().insert(randomUser);

        appDb.userDao()
                .loadUserId(randomUser.id)
                .test()
                .assertValue(user -> user.id == randomUser.id &&
                        user.userName.equals(randomUser.userName) &&
                        user.mail.equals(randomUser.mail));

    }

    @Test
    public void insertAndGetPostComments() {

        final PostComments randomPostComments = randomPostComments();
        appDb.postCommentsDao().insert(randomPostComments);

        appDb.postCommentsDao()
                .load(randomPostComments.postId)
                .test()
                .assertValue(postCmt -> postCmt.postId == randomPostComments.postId && postCmt.numOfComments == randomPostComments.numOfComments);

    }

    @Test
    public void clearUsersAndPostsTest() {
        final PostComments randomPostComments = randomPostComments();
        appDb.postCommentsDao().insert(randomPostComments);
        final User randomUser = randomUser();
        appDb.userDao().insert(randomUser);

        appDb.userDao().deleteAll();
        appDb.postCommentsDao().deleteAll();

        appDb.userDao().loadAll().test().assertValue(users -> users.isEmpty());

        appDb.postCommentsDao().loadAll().test().assertValue(postComments -> postComments.isEmpty());


    }


    private PostComments randomPostComments() {
        return new PostComments(25, 3);
    }


    private User randomUser() {
        return new User(7777, "lakis", "lakis@lakis.com");
    }

}
