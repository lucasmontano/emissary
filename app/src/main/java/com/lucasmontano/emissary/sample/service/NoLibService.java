package com.lucasmontano.emissary.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.TimeZone;


public class NoLibService extends Service {

    /**
     * Time Zone
     */
    public static final String ARG_TIME_ZONE = "ARG_TIME_ZONE";

    /**
     * Whats
     */
    public static final int WHAT_TIME_ZONE = 1;
    public static final int REPLY_TO = 0;

    /**
     * Reply to activity with this messeger
     */
    private Messenger replyMesseger;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case REPLY_TO:
                    replyMesseger = message.replyTo;
                    return true;
                case WHAT_TIME_ZONE:

                    String timeZone = TimeZone.getDefault().getDisplayName();

                    Toast.makeText(getApplicationContext(), timeZone, Toast.LENGTH_SHORT).show();

                    if (replyMesseger != null) {

                        Message messageToActivity = new Message();
                        Bundle data = new Bundle();
                        data.putString(ARG_TIME_ZONE, timeZone);
                        messageToActivity.setData(data);
                        messageToActivity.what = WHAT_TIME_ZONE;
                        try {
                            replyMesseger.send(messageToActivity);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
            }

            return false;
        }
    });

    Messenger messenger = new Messenger(handler);

    public NoLibService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
