package com.lucasmontano.emissary.sample.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lucasmontano.emissary.R;
import com.lucasmontano.emissary.sample.service.NoLibService;


public class NoLibActivity extends ActionBarActivity {

    /**
     * Speak to Bound Service
     */
    private Messenger serviceMessenger;

    /**
     * Service reply to this messeger;
     */
    private Messenger activityMesseger = new Messenger(new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case NoLibService.WHAT_TIME_ZONE:
                    ((TextView) findViewById(R.id.time_zone)).setText(message.getData().getString(NoLibService.ARG_TIME_ZONE));
                    return true;
            }
            return false;
        }
    }));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_replyto);

        /**
         * Bind Activity to Service
         */
        Intent intent = new Intent(this, NoLibService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    /**
     * Connection to Service
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            serviceMessenger = new Messenger(iBinder);

            /**
             * Ask to reply
             */
            Message message = new Message();
            message.what = NoLibService.REPLY_TO;
            message.replyTo = activityMesseger;

            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        /**
         * Bind on Create, Unbind on Destroy
         */
        if (serviceMessenger != null) {
            unbindService(serviceConnection);
        }
        serviceConnection = null;
        serviceMessenger = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_service, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_test) {
            showServiceTimeZone();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showServiceTimeZone() {
        if (serviceMessenger != null) {
            Message message = new Message();
            message.what = NoLibService.WHAT_TIME_ZONE;

            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
