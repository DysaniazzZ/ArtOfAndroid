package com.dysania.artofandroid.chapter02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.dysania.artofandroid.chapter02.aidl.BookManagerActivity;
import com.dysania.artofandroid.chapter02.messenger.MessengerActivity;
import com.dysania.artofandroid.chapter02.model.User;
import com.dysania.artofandroid.chapter02.provider.ProviderActivity;
import com.dysania.artofandroid.chapter02.utils.MyConstants;
import com.dysania.artofandroid.chapter02.utils.MyUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.btn_first).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_messenger).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MessengerActivity.actionStart(FirstActivity.this);
            }
        });

        findViewById(R.id.btn_bookmanager).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BookManagerActivity.actionStart(FirstActivity.this);
            }
        });

        findViewById(R.id.btn_provider).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProviderActivity.actionStart(FirstActivity.this);
            }
        });

        Log.d(TAG, "UserId: " + UserManager.sUserId);
        UserManager.sUserId = 2;
        Log.d(TAG, "UserId: " + UserManager.sUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //将User序列化到硬盘
        persistToFile();
    }

    private void persistToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(1, "William", true);
                File dir = new File(MyConstants.CHAPTER_2_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File cacheFile = new File(MyConstants.CACHE_FILE_PATH);
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cacheFile));
                    objectOutputStream.writeObject(user);
                    Log.d(TAG, "persist user: " + user);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    MyUtil.close(objectOutputStream);
                }
            }
        }).start();
    }
}
