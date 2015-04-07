package com.lucasmontano.emissary;

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
final class ServiceToActivity implements Emissary.IEmissary {

    private final HashMap<Integer, Emissary.EmissaryMessengerCallback> whats = new HashMap<Integer, Emissary.EmissaryMessengerCallback>();

    private final Handler handler;

    private Messenger messenger;
    private Messenger replyTo;

    public ServiceToActivity() {

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                if (whats.containsKey(message.what)) {
                    whats.get(message.what).data(message.getData());
                    return true;
                }

                switch (message.what) {
                    case ActivityToService.REPLY_TO:
                        replyTo = message.replyTo;
                        return true;
                }

                return false;
            }
        });

        this.messenger = new Messenger(handler);
    }

    @Override
    public void subscribe(int what, Emissary.EmissaryMessengerCallback receiverCallback) {
        whats.put(what, receiverCallback);
    }

    @Override
    public void request(int what, Emissary.EmissaryMessengerCallback callback) {

    }

    @Override
    public IBinder getIBinder() {
        return messenger.getBinder();
    }

    @Override
    public void send(int what, Bundle bundle) {

        if (replyTo == null) {
            return;
        }

        Message messageToActivity = new Message();
        messageToActivity.setData(bundle);
        messageToActivity.what = what;

        try {
            replyTo.send(messageToActivity);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBound() {
        return getIBinder().isBinderAlive();
    }

    @Override
    public ServiceConnection getServiceConnection() {
        return null;
    }
}
