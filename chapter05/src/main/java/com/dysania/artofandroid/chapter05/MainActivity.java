package com.dysania.artofandroid.chapter05;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private LinearLayout mLlRemoteViewsContainer;
    private NotificationManager mNotificationManager;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Receive remote msg", Toast.LENGTH_SHORT).show();
            RemoteViews remoteViews = intent.getParcelableExtra(DemoActivity.EXTRA_REMOTE_VIEWS);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_default).setOnClickListener(this);
        findViewById(R.id.btn_custom).setOnClickListener(this);
        findViewById(R.id.btn_demo).setOnClickListener(this);

        mLlRemoteViewsContainer = (LinearLayout) findViewById(R.id.ll_remote_views);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        IntentFilter intentFilter = new IntentFilter(DemoActivity.ACTION_REMOTE_VIEWS);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_default:
                showDefaultNotification();
                break;
            case R.id.btn_custom:
                showCustomNotification();
                break;
            case R.id.btn_demo:
                DemoActivity.actionStart(this);
                break;
        }
    }

    private void showDefaultNotification() {
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Hello World")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, DemoActivity.class),
                        PendingIntent.FLAG_CANCEL_CURRENT))
                .setContentTitle("Chapter05")
                .setContentText("This is a notification")
                .build();

        mNotificationManager.notify(1, notification);
    }

    private void showCustomNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.msg, "Chapter05");                     //更新RemoteViews的文本
        remoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);      //更新RemoteViews的图片
        remoteViews.setOnClickPendingIntent(R.id.open_activity,
                PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, DemoActivity.class),
                        PendingIntent.FLAG_CANCEL_CURRENT));                    //给RemoteViews添加点击事件

        Notification notification = new Builder(this)
                .setTicker("Hello World")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContent(remoteViews)
                .build();

        mNotificationManager.notify(2, notification);
    }

    private void updateUI(RemoteViews remoteViews) {
        //Note: 由于含有ImageView，可能会出现如下错误：AppCompatImageView can't use method with RemoteViews: setImageResource(int)
        //这时需要把appcompat-v7包调到23.0.1或之前

        //适用于同一个应用的两个进程
//        View view = remoteViews.apply(this, mLlRemoteViewsContainer);
//        mLlRemoteViewsContainer.addView(view);

        //适用于不同应用的两个进程
        int layoutId = getResources().getIdentifier("layout_notification", "layout", getPackageName());
        View view = getLayoutInflater().inflate(layoutId, mLlRemoteViewsContainer, false);
        remoteViews.reapply(this, view);
        mLlRemoteViewsContainer.addView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
