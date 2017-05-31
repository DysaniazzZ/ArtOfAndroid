package com.dysania.artofandroid.chapter02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.dysania.artofandroid.chapter02.model.User;
import com.dysania.artofandroid.chapter02.utils.MyConstants;
import com.dysania.artofandroid.chapter02.utils.MyUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by DysaniazzZ on 22/05/2017.
 */

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.btn_second).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        Log.d(TAG, "UserId: " + UserManager.sUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //将User反序列化到内存
        recoverFromFile();
    }

    private void recoverFromFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = null;
                File cacheFile = new File(MyConstants.CACHE_FILE_PATH);
                if (cacheFile.exists()) {
                    ObjectInputStream objectInputStream = null;
                    try {
                        objectInputStream = new ObjectInputStream(new FileInputStream(cacheFile));
                        user = (User) objectInputStream.readObject();
                        Log.d(TAG, "recover user: " + user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        MyUtil.close(objectInputStream);
                    }
                }
            }
        }).start();
    }
}
