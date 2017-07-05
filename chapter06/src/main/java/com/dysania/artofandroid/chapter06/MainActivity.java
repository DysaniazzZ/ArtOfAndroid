package com.dysania.artofandroid.chapter06;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_transition).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionDrawable drawable = (TransitionDrawable) v.getBackground();
                drawable.startTransition(3000);
            }
        });

        ScaleDrawable scaleDrawable = (ScaleDrawable) findViewById(R.id.v_scale).getBackground();
        scaleDrawable.setLevel(10);      //lever默认为0，无法显示。level范围为0~10000。level越大，显示的越大

        ClipDrawable clipDrawable = (ClipDrawable) findViewById(R.id.v_clip).getBackground();
        clipDrawable.setLevel(5000);
    }
}
