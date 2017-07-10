package com.dysania.artofandroid.chapter08;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    private Button mFloatingButton;

    private int mLastX;
    private int mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFloatingButton = new Button(this);
        mFloatingButton.setText("Hello");

        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        mLayoutParams.type = LayoutParams.TYPE_TOAST;
        mLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 100;
        mLayoutParams.y = 300;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingButton, mLayoutParams);

        mFloatingButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.x += rawX - mLastX;
                        mLayoutParams.y += rawY - mLastY;
                        mWindowManager.updateViewLayout(mFloatingButton, mLayoutParams);
                        break;
                    default:
                        break;
                }
                mLastX = rawX;
                mLastY = rawY;
                return false;
            }
        });
    }
}
