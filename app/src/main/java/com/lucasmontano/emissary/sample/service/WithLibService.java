package com.lucasmontano.emissary.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.lucasmontano.emissary.Emissary;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class WithLibService extends Service {

    /**
     * Time Zone
     */
    public static final String ARG_TIME_ZONE = "ARG_TIME_ZONE";

    /**
     * Whats
     */
    public static final int ON_CHANGE_TIME_ZONE = 1;
    public static final int GET_TIME_ZONE = 2;

    private final Emissary.IEmissary emissary;

    public WithLibService() {
        emissary = Emissary.getInstance(this);
    }

    public void onCreate() {
        super.onCreate();

        emissary.subscribe(GET_TIME_ZONE, new Emissary.EmissaryMessengerCallback() {
            @Override
            public void data(Bundle data) {
                String timeZone = TimeZone.getDefault().getDisplayName();
                Bundle bundle = new Bundle();
                bundle.putString(WithLibService.ARG_TIME_ZONE, timeZone);
                emissary.send(GET_TIME_ZONE, bundle);
            }
        });

        /**
         * Just for sample
         */
        TimerTask scanTask;
        final Handler handler = new Handler();
        Timer t = new Timer();
        scanTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        String timeZone = TimeZone.getDefault().getDisplayName();
                        Bundle bundle = new Bundle();
                        bundle.putString(WithLibService.ARG_TIME_ZONE, timeZone);
                        emissary.send(ON_CHANGE_TIME_ZONE, bundle);
                    }
                });
            }};
        t.schedule(scanTask, 300, 5000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return emissary.getIBinder();
    }
}
