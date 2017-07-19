package com.dysania.artofandroid.chapter13;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by DysaniazzZ on 19/07/2017.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //install multi dex support
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常处理器
        CrashHandler.getInstance().init(this);
    }
}
