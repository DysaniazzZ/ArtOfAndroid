package com.dysania.artofandroid.chapter11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_TASK01 = "com.dysania.artorandroid.chapter11.action_TASK01";
    public static final String ACTION_TASK02 = "com.dysania.artorandroid.chapter11.action_TASK02";
    public static final String ACTION_TASK03 = "com.dysania.artorandroid.chapter11.action_TASK03";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runIntentService();
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
}
