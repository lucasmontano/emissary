package com.lucasmontano.services.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.lucasmontano.services.utils.ServiceConnectionHelper;
import com.lucasmontano.services.utils.ServiceMessengerHelper;

import java.util.TimeZone;


public class MyServiceSimple extends Service {

    /**
     * Time Zone
     */
    public static final String ARG_TIME_ZONE = "ARG_TIME_ZONE";

    /**
     * Whats
     */
    public static final int ON_CHANGE_TIME_ZONE = 1;
    public static final int GET_TIME_ZONE = 2;

    /**
     * Reply to activity with this messeger
     */
    private ServiceMessengerHelper replyMesseger;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case ServiceConnectionHelper.REPLY_TO:
                    replyMesseger = new ServiceMessengerHelper(message.replyTo);
                    return true;
                case GET_TIME_ZONE:

                    String timeZone = TimeZone.getDefault().getDisplayName();

                    if (replyMesseger != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(MyServiceSimple.ARG_TIME_ZONE, timeZone);
                        replyMesseger.send(GET_TIME_ZONE, bundle);
                    }

                    return true;
            }

            return false;
        }
    });

    Messenger messenger = new Messenger(handler);

    public MyServiceSimple() {
    }

    public void onCreate() {
        super.onCreate();

        String timeZone = TimeZone.getDefault().getDisplayName();

        Bundle bundle = new Bundle();
        bundle.putString(MyServiceSimple.ARG_TIME_ZONE, timeZone);
        replyMesseger.send(ON_CHANGE_TIME_ZONE, bundle);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
