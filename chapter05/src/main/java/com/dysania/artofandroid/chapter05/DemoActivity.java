package com.dysania.artofandroid.chapter05;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

/**
 * Created by DysaniazzZ on 03/07/2017.
 */

public class DemoActivity extends AppCompatActivity {

    public static final String ACTION_REMOTE_VIEWS = "com.dysania.artofandroid.chapter05.action_REMOTE";
    public static final String EXTRA_REMOTE_VIEWS = "extra_remote_views";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.btn_mock).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMockNotification();
            }
        });
    }

    private void changeMockNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.msg, "msg from process: " + Process.myPid());
        remoteViews.setImageViewResource(R.id.icon, R.drawable.ic_dysania);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, DemoActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.open_activity, pendingIntent);

        Intent intent = new Intent(ACTION_REMOTE_VIEWS);
        intent.putExtra(EXTRA_REMOTE_VIEWS, remoteViews);
        sendBroadcast(intent);
    }
}
