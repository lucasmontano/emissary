package com.lucasmontano.emissary;

import android.app.Activity;
import android.app.Service;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by montanlu on 07/04/2015.
 */
public class Emissary {

    /**
     * Interface used for receive and send information
     */
    public interface EmissaryMessengerCallback {

        /**
         *
         * @see android.os.Bundle
         *
         * @param data
         */
        public void data(Bundle data);
    }

    public interface IEmissary {

        /**
         *
         * Subscribe to listen messages
         *
         * @param postageStamp
         * @param callback
         */
        public void subscribe(int postageStamp, EmissaryMessengerCallback callback);

        /**
         *
         * Request a message.
         *
         * @param postageStamp
         * @param callback
         */
        public void request(int postageStamp, EmissaryMessengerCallback callback);

        /**
         *
         * Send message, this message will be returned for requested ou subscribed
         *
         * @param postageStamp
         * @param data
         */
        public void send(int postageStamp, Bundle data);

        /**
         *
         * Return the binder interface.
         *
         * @see android.os.IBinder
         *
         * @return IBinder
         */
        public IBinder getIBinder();

        /**
         *
         * Return TRUE if service is bound to activity
         *
         * @return boolean
         */
        public boolean isBound();

        /**
         *
         * Return the service connection. Only for activity.
         *
         * @see android.content.ServiceConnection
         *
         * @return ServiceConnection
         */
        public ServiceConnection getServiceConnection();
    }

    /**
     *
     * @param activity
     *
     * @return IEmissary
     */
    public static IEmissary getInstance(Activity activity) {
        return new ActivityToService();
    }

    /**
     *
     * @param service
     *
     * @return IEmissary
     */
    public static IEmissary getInstance(Service service) {
        return new ServiceToActivity();
    }
}
