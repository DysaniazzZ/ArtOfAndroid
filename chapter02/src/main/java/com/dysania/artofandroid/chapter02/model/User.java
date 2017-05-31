package com.dysania.artofandroid.chapter02.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.dysania.artofandroid.chapter02.aidl.Book;
import java.io.Serializable;

/**
 * Created by DysaniazzZ on 23/05/2017.
 */

public class User implements Serializable, Parcelable {

    public int userId;
    public String userName;
    public boolean isMale;

    public Book book;

    public User(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readByte() != 0;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeByte((byte) (isMale ? 1 : 0));
        dest.writeParcelable(book, 0);
    }

    @Override
    public String toString() {
        return String.format("User:{userId:%s, userName:%s, isMale:%s}, with child:{%s}", userId, userName, isMale, book);
    }
}
