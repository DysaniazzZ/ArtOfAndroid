package com.dysania.artofandroid.chapter13;

import android.app.Application;

/**
 * Created by DysaniazzZ on 19/07/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常处理器
        CrashHandler.getInstance().init(this);
    }
}
