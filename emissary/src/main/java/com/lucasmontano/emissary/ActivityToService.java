package com.lucasmontano.emissary;

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
final class ActivityToService implements ServiceConnection, Emissary.IEmissary {

    private Messenger messenger;

    public static final int REPLY_TO = 1989;

    private final HashMap<Integer, Emissary.EmissaryMessengerCallback> whats = new HashMap<Integer, Emissary.EmissaryMessengerCallback>();

    private IBinder mBinder;

    public ActivityToService() {

    }

    @Override
    public void subscribe(int what, Emissary.EmissaryMessengerCallback receiverCallback) {
        whats.put(what, receiverCallback);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        Messenger activityMesseger = new Messenger(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message message) {
                if (whats.containsKey(message.what)) {
                    whats.get(message.what).data(message.getData());
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

    @Override
    public IBinder getIBinder() {
        return mBinder;
    }

    @Override
    public boolean isBound() {
        return messenger != null;
    }

    @Override
    public ServiceConnection getServiceConnection() {
        return this;
    }

    @Override
    public void request(int what, Emissary.EmissaryMessengerCallback callback) {

        subscribe(what, callback);

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

    @Override
    public void send(int what, Bundle data) {

    }
}
