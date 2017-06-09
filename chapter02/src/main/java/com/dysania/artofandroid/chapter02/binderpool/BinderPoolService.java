package com.dysania.artofandroid.chapter02.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by DysaniazzZ on 09/06/2017.
 */

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";
    
    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinderPool;
    }
}
