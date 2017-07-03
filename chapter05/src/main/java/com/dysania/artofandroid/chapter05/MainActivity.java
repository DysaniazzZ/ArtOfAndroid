package com.dysania.artofandroid.chapter05;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_default).setOnClickListener(this);
        findViewById(R.id.btn_custom).setOnClickListener(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
        remoteViews.setOnClickPendingIntent(R.id.open_activity, PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, DemoActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT));                            //给RemoteViews添加点击事件

        Notification notification = new Builder(this)
                .setTicker("Hello World")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContent(remoteViews)
                .build();

        mNotificationManager.notify(2, notification);
    }
}
