package com.lucasmontano.services.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by lucasmontano on 05/04/15.
 */
public class ServiceMessengerHelper {

    private Messenger messenger;

    public ServiceMessengerHelper(Messenger messenger) {
        this.messenger = messenger;
    }

    public void send(int what, Bundle bundle) {
        if (messenger == null) {
            return;
        }

        Message messageToActivity = new Message();
        messageToActivity.setData(bundle);
        messageToActivity.what = what;

        try {
            messenger.send(messageToActivity);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
