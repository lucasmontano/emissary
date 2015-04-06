package com.lucasmontano.services.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.lucasmontano.services.R;
import com.lucasmontano.services.service.MyServiceSimple;
import com.lucasmontano.services.utils.ServiceConnectionHelper;


public class ServiceSimpleActivity extends ActionBarActivity {

    /**
     * Simple Service Messenger
     */
    ServiceConnectionHelper simpleServiceConnection = new ServiceConnectionHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_replyto);

        /**
         * Bind Activity to Service
         */
        Intent intent = new Intent(this, MyServiceSimple.class);
        bindService(intent, simpleServiceConnection, Service.BIND_AUTO_CREATE);

        /**
         * Event Listener
         */
        simpleServiceConnection.addEventListener(MyServiceSimple.ON_CHANGE_TIME_ZONE, new ServiceConnectionHelper.Callback() {
            @Override
            public void onReceive(Bundle data) {
                ((TextView) findViewById(R.id.time_zone)).setText(data.getString(MyServiceSimple.ARG_TIME_ZONE));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /**
         * Bind on Create, Unbind on Destroy
         */
        if (simpleServiceConnection.isBound()) {
            unbindService(simpleServiceConnection);
        }
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

    /**
     * Ask TimeZone
     */
    public void showServiceTimeZone() {
        simpleServiceConnection.request(MyServiceSimple.GET_TIME_ZONE, new ServiceConnectionHelper.Callback() {
            @Override
            public void onReceive(Bundle data) {
                Toast.makeText(ServiceSimpleActivity.this, data.getString(MyServiceSimple.ARG_TIME_ZONE), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
