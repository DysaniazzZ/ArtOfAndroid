package com.dysania.artofandroid.chapter02.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.dysania.artofandroid.chapter02.aidl.Book;
import com.dysania.artofandroid.chapter02.model.User;

/**
 * Created by DysaniazzZ on 07/06/2017.
 */

public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = "ProviderActivity";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ProviderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Uri uri = Uri.parse("content://com.dysania.artofandroid.chapter02.provider");
//        getContentResolver().query(uri, null, null, null, null);
//        getContentResolver().query(uri, null, null, null, null);
//        getContentResolver().query(uri, null, null, null, null);

        Uri bookUri = Uri.parse("content://com.dysania.artofandroid.chapter02.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "Symbian");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Log.d(TAG, "query book: " + book.toString());
        }
        bookCursor.close();

        Uri userUri = Uri.parse("content://com.dysania.artofandroid.chapter02.provider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.userId = userCursor.getInt(0);
            user.userName = userCursor.getString(1);
            user.isMale = userCursor.getInt(2) == 1;
            Log.d(TAG, "quert user: " + user.toString());
        }
        userCursor.close();
    }
}
