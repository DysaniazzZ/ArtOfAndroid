package com.dysania.artorandroid.chapter07;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by DysaniazzZ on 06/07/2017.
 */

public class PropertyAnimActivity extends AppCompatActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PropertyAnimActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim);

        findViewById(R.id.tv_object_animator).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ObjectAnimator.ofFloat(v, "translationY", -v.getHeight(), v.getHeight()).start();    //默认时间是100ms

                ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater
                        .loadAnimator(v.getContext(), R.animator.object_anim_example);
                objectAnimator.setTarget(v);
                objectAnimator.start();
            }
        });

        findViewById(R.id.tv_value_animator).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(v.getContext(), R.animator.value_anim_example);
                valueAnimator.setTarget(v);
                valueAnimator.start();
            }
        });

        findViewById(R.id.tv_set_animator).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet set = new AnimatorSet();
                //set.playSequentially()这个方法是按照顺序依次播放动画
                //set.playTogether()这个方法是一起播放所有的动画
                set.playTogether(
                        ObjectAnimator.ofFloat(v, "rotationY", 0, 360),
                        ObjectAnimator.ofFloat(v, "translationY", 0, 90),
                        ObjectAnimator.ofFloat(v, "scaleX", 0.5f, 2.0f),
                        ObjectAnimator.ofFloat(v, "scaleY", 0.5f, 2.0f),
                        ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.5f, 1.0f)
                );
                set.setDuration(1500).start();
            }
        });
    }
}
