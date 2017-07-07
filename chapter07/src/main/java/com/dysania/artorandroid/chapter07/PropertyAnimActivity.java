package com.dysania.artorandroid.chapter07;

import android.animation.ObjectAnimator;
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
                ObjectAnimator.ofFloat(v, "translationY", -v.getHeight(), v.getHeight()).start();    //默认时间是100ms

//                ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(v.getContext(), R.animator.object_anim_example);
//                objectAnimator.setTarget(v);
//                objectAnimator.start();
            }
        });
    }
}
