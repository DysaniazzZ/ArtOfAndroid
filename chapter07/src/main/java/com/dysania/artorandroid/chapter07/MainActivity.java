package com.dysania.artorandroid.chapter07;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_view_anim).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAnimActivity.actionStart(MainActivity.this);
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);     //必须设置在startActivity之后
            }
        });

        findViewById(R.id.btn_frame_anim).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameAnimActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.btn_layout_anim).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutAnimActivity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.btn_property_anim).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyAnimActivity.actionStart(MainActivity.this);
            }
        });
    }
}
