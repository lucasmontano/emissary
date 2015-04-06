package com.lucasmontano.services.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lucasmontano.services.R;
import com.lucasmontano.services.service.MyService;


public class ServiceActivity extends ActionBarActivity {

    /**
     * Speak to Bound Service
     */
    private Messenger serviceMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        /**
         * Bind Activity to Service
         */
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    /**
     * Connection to Service
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceMessenger = new Messenger(iBinder);
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
            message.what = MyService.WHAT_TIME_ZONE;

            try {
                serviceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
