package com.dysania.artofandroid.chapter03;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dysania.artofandroid.chapter03.view.ScrollerView;

public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;

    private View mHandlerView;
    private int mCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO:
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        mHandlerView.scrollTo(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.slide_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello World!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.scroller_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Scroller
                ((ScrollerView) v).smoothScrollTo(100);
            }
        });

        findViewById(R.id.anim_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                //属性动画
//                ObjectAnimator.ofFloat(v, "translationX", 0, 100).setDuration(1000).start();        //改变的是View的位置

                final int startX = 0;                                                                 //改变的是View内容的位置
                final int deltaX = 100;
                ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
                animator.addUpdateListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedFraction = animation.getAnimatedFraction();
                        v.scrollTo((startX + (int) (deltaX * animatedFraction)), 0);
                    }
                });
                animator.start();
            }
        });

        mHandlerView = findViewById(R.id.handler_view);
        mHandlerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handler
                mCount = 0;
                mHandler.sendEmptyMessage(MESSAGE_SCROLL_TO);
            }
        });

        findViewById(R.id.btn_demo1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Demo1Activity.actionStart(MainActivity.this);
            }
        });

        findViewById(R.id.btn_demo2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Demo2Activity.actionStart(MainActivity.this);
            }
        });
    }
}
