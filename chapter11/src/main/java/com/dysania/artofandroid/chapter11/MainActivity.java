package com.dysania.artofandroid.chapter11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ACTION_TASK01 = "com.dysania.artorandroid.chapter11.action_TASK01";
    public static final String ACTION_TASK02 = "com.dysania.artorandroid.chapter11.action_TASK02";
    public static final String ACTION_TASK03 = "com.dysania.artorandroid.chapter11.action_TASK03";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runIntentService();
        runThreadPool();
    }

    private void runIntentService() {
        //每执行一个后台任务就要启动一次IntentService
        Intent intent = new Intent(this, LocalIntentService.class);
        intent.putExtra("task_action", ACTION_TASK01);
        startService(intent);
        intent.putExtra("task_action", ACTION_TASK02);
        startService(intent);
        intent.putExtra("task_action", ACTION_TASK03);
        startService(intent);
    }

    private void runThreadPool() {
        Runnable command = new Runnable(){
            @Override
            public void run() {
                Log.d(TAG, Thread.currentThread().getName());
            }
        };

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        fixedThreadPool.execute(command);

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(command);

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
        //scheduledThreadPool.schedule(command, 2, TimeUnit.SECONDS);                             //延迟2秒后执行
        scheduledThreadPool.scheduleAtFixedRate(command, 10, 2000, TimeUnit.MILLISECONDS);      //延迟10ms后，每隔1000ms执行一次

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(command);
    }
}
