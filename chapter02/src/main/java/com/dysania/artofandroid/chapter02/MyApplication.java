package com.dysania.artofandroid.chapter02;

import android.app.Application;
import android.os.Process;
import android.util.Log;
import com.dysania.artofandroid.chapter02.utils.MyUtil;

/**
 * Created by DysaniazzZ on 22/05/2017.
 */

public class MyApplication extends Application{

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = MyUtil.getProcessName(getApplicationContext(), Process.myPid());
        Log.d(TAG, "Application start, process name: " + processName);
    }
}
