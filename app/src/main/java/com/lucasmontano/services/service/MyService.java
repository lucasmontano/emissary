package com.lucasmontano.services.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.util.TimeZone;


public class MyService extends Service {

    public static final int WHAT_TIME_ZONE = 1;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case WHAT_TIME_ZONE:
                    Toast.makeText(getApplicationContext(), TimeZone.getDefault().getDisplayName(), Toast.LENGTH_SHORT).show();
                    return true;
            }

            return false;
        }
    });

    Messenger messenger = new Messenger(handler);

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
