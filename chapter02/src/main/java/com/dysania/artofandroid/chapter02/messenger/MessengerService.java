package com.dysania.artofandroid.chapter02.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.dysania.artofandroid.chapter02.utils.MyConstants;

/**
 * Created by DysaniazzZ on 26/05/2017.
 */

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.d(TAG, "receive msg from client: " + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message message = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                    Bundle data = new Bundle();
                    data.putString("reply", "Okay, your message is received, I will reply later.");
                    message.setData(data);
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
