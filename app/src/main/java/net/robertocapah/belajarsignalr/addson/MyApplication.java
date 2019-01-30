package net.robertocapah.belajarsignalr.addson;

import android.app.Application;

/**
 * Created by ASUS ZE on 26/12/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public void unsetConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = null;
    }
    public void setSignalRListener(ConnectivityReceiver.SignalRReceiverListener listener) {
        ConnectivityReceiver.signalRReceiverListener = listener;
    }
}
