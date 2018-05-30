package com.babylon.testproject.data.responses;

import com.babylon.testproject.data.models.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserResp {
    @Expose
    @SerializedName("username")
    public final String userName;
    @Expose
    @SerializedName("email")
    public final String mail;
    @Expose
    @SerializedName("name")
    public final String name;
    @Expose
    @SerializedName("id")
    public final long id;

    public UserResp(long id, String userName, String mail, String name) {
        this.userName = userName;
        this.mail = mail;
        this.name = name;
        this.id = id;
    }

    public User map() {
        return new User(id, userName, mail);
    }

}
