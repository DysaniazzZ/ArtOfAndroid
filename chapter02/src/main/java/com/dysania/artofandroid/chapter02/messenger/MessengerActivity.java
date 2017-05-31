package com.dysania.artofandroid.chapter02.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.dysania.artofandroid.chapter02.utils.MyConstants;

/**
 * Created by DysaniazzZ on 26/05/2017.
 */

public class MessengerActivity extends AppCompatActivity {

    private Messenger mService;
    private static final String TAG = "MessengerActivity";

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_SERVICE:
                    Log.d(TAG, "receive msg from service: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "Hello, this is client.");
            msg.setData(data);

            msg.replyTo = mGetReplyMessenger;

            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MessengerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
