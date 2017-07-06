package com.dysania.artorandroid.chapter07;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by DysaniazzZ on 06/07/2017.
 */

public class ViewAnimActivity extends AppCompatActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ViewAnimActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_anim);

        findViewById(R.id.tv_translate).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ViewAnimActivity.this, R.anim.translate_example);
                v.startAnimation(animation);
            }
        });

        findViewById(R.id.tv_scale).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ViewAnimActivity.this, R.anim.scale_example);
                v.startAnimation(animation);
            }
        });

        findViewById(R.id.tv_rotate).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ViewAnimActivity.this, R.anim.rotate_example);
                v.startAnimation(animation);
            }
        });

        findViewById(R.id.tv_alpha).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ViewAnimActivity.this, R.anim.alpha_example);
                v.startAnimation(animation);
            }
        });

        findViewById(R.id.tv_set).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(ViewAnimActivity.this, R.anim.set_example);
                v.startAnimation(animation);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);     //必须设置在finish之后
    }
}
