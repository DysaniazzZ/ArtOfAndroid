package com.dysania.artofandroid.chapter02.binderpool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by DysaniazzZ on 09/06/2017.
 */

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = "BinderPoolActivity";

    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BinderPoolActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG, "visit ISecurityCenter");

        String msg = "Hello-安卓";
        System.out.println("content: " + msg);

        try {
            String password = mSecurityCenter.encrypt(msg);
            System.out.println("encrypt: " + password);
            System.out.println("decrypt: " + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = ComputeImpl.asInterface(computeBinder);
        Log.d(TAG, "visit ICompute");

        try {
            System.out.println("3 + 5 = " + mCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
