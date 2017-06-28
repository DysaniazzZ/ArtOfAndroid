package com.dysania.artofandroid.chapter03.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by DysaniazzZ on 13/06/2017.
 */

public class SlideView extends View {

    private static final String TAG = "SlideView";

    //系统能识别的滑动最小距离
    private int mScaledTouchSlop;

    //记录上次滑动的坐标
    private float mLastX = 0;
    private float mLastY = 0;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.d(TAG, "ScaledTouchSlop is " + mScaledTouchSlop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastX;
                float deltaY = y - mLastY;
                Log.d(TAG, "move, deltaX: " + deltaX + "\t deltaY: " + deltaY);
                float translationX = getTranslationX() + deltaX;
                float translationY = getTranslationY() + deltaY;
                setTranslationX(translationX);
                setTranslationY(translationY);
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(event);
    }
}
