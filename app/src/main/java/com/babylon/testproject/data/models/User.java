package com.babylon.testproject.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.babylon.testproject.data.database.DatabaseConfig;

/**
 * Created by Antonis Latas on 10/24/2017
 */
@Entity(tableName = DatabaseConfig.USER_TABLE_NAME)
public class User {
    public final String mail;
    @PrimaryKey
    public final long id;
    public final String userName;


    public User(long id, String userName, String mail) {
        this.mail = mail;
        this.id = id;
        this.userName = userName;
    }

    @Ignore
    public User() {
        this(-Integer.MAX_VALUE, "", "");
    }


    public String mail() {
        return mail;
    }


    @Override
    public String toString() {
        return userName;
    }

    public long id() {
        return id;
    }

    public boolean isValid() {
        return id() >= 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof User))
            return false;
        return id == ((User) obj).id && mail.equals(((User) obj).mail) && userName.equals(((User) obj).userName);

    }
}
