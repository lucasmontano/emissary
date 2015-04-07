package com.lucasmontano.emissary.sample.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.lucasmontano.emissary.R;
import com.lucasmontano.emissary.sample.service.WithLibService;
import com.lucasmontano.emissary.services.Emissary;

public class WithLibActivity extends ActionBarActivity {

    private Emissary.IEmissary emissary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_replyto);

        emissary = Emissary.getInstance(this);

        /**
         * Bind Activity to Service
         */
        Intent intent = new Intent(this, WithLibService.class);
        bindService(intent, emissary.getServiceConnection(), Service.BIND_AUTO_CREATE);

        /**
         * Event Listener
         */
        emissary.subscribe(WithLibService.ON_CHANGE_TIME_ZONE, new Emissary.EmissaryMessengerCallback() {

            @Override
            public void data(Bundle data) {
                ((TextView) findViewById(R.id.time_zone)).setText(data.getString(WithLibService.ARG_TIME_ZONE));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /**
         * Bind on Create, Unbind on Destroy
         */
        if (emissary.isBound()) {
            unbindService(emissary.getServiceConnection());
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
        emissary.request(WithLibService.GET_TIME_ZONE, new Emissary.EmissaryMessengerCallback() {

            @Override
            public void data(Bundle data) {
                Toast.makeText(WithLibActivity.this, data.getString(WithLibService.ARG_TIME_ZONE), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
