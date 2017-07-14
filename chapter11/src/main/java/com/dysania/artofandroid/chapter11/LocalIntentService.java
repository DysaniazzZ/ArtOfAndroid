package com.dysania.artofandroid.chapter11;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by DysaniazzZ on 14/07/2017.
 */

public class LocalIntentService extends IntentService {

    private static final String TAG = "LocalIntentService";

    public LocalIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getStringExtra("task_action");
        Log.d(TAG, "receive action: " + action);
        SystemClock.sleep(2000);
        if (MainActivity.ACTION_TASK01.equals(action)) {
            //TODO 这里执行后台任务
            Log.d(TAG, "handle task: " + action);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //所有任务执行完后，会自动停止服务
        Log.d(TAG, "LocalIntentService destroyed");
    }
}
