package com.dysania.artofandroid.chapter15;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.root_layout).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        findViewById(R.id.include_layout).setBackgroundColor(getResources().getColor(R.color.colorAccent));

        //加载ViewStub的两种方式
//        findViewById(R.id.viewstub_layout).setVisibility(View.VISIBLE);
        View inflateView = ((ViewStub)findViewById(R.id.viewstub_layout)).inflate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                testANR();
            }
        }).start();
        SystemClock.sleep(100);
        initView();
    }

    private synchronized void testANR() {
        SystemClock.sleep(30000);
    }

    private synchronized void initView() {
    }
}
