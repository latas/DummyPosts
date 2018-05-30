package com.babylon.testproject.data.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Post implements Parcelable {

    public final long id;
    public final long userId;
    public final String text;
    public final String title;

    public Post(long id, String title, String text, long userId) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.title = title;
    }


    public String getText() {
        return text;
    }

    public long userId() {
        return userId;
    }


    protected Post(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        text = in.readString();
        title = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeString(text);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

}
