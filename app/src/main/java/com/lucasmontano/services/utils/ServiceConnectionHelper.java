package com.lucasmontano.services.utils;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.HashMap;

/**
 * Created by lucasmontano on 05/04/15.
 */
public final class ServiceConnectionHelper implements ServiceConnection {

    private Messenger messenger;

    public static final int REPLY_TO = 1989;

    private final HashMap<Integer, Callback> whats = new HashMap<Integer, Callback>();

    private IBinder mBinder;

    public interface Callback {
        public void onReceive(Bundle data);
    }

    public ServiceConnectionHelper() {

    }

    public void addEventListener(int what, Callback receiverCallback) {
        whats.put(what, receiverCallback);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        Messenger activityMesseger = new Messenger(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message message) {
                if (whats.containsKey(message.what)) {
                    whats.get(message.what).onReceive(message.getData());
                    return true;
                }
                return false;
            }
        }));

        /**
         * Ask to reply
         */
        Message message = new Message();
        message.what = REPLY_TO;
        message.replyTo = activityMesseger;

        mBinder = iBinder;

        try {
            messenger = new Messenger(iBinder);
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public IBinder getIBinder() {
        return mBinder;
    }

    public boolean isBound() {
        return messenger != null;
    }

    public void request(int what, Callback callback) {

        addEventListener(what, callback);

        if (messenger != null) {
            Message message = new Message();
            message.what = what;

            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
