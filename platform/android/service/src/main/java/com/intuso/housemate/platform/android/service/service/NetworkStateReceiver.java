package com.intuso.housemate.platform.android.service.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by tomc on 02/06/14.
 *
 * Socket client should handle all this by itself anyway, but when it can't connect it backs off which means it may take
 * a while to connect again when the network becomes available. By handling this here, we can tell it to connect as soon
 * as the network is available.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private final static String TAG = "PlatformNetworkState";

    public void onReceive(Context context, Intent intent) {
        debugIntent(intent);
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            boolean connected = ni != null && ni.isConnectedOrConnecting();
            if(!connected) {
                Log.d(TAG, "Connected false, checking other network info");
                ni = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
                connected = ni != null && ni.isFailover();
            }
            Log.d(TAG, "Parsed connected as " + connected);
            Intent serviceIntent = new Intent(context, ConnectionService.class);
            serviceIntent.setAction(ConnectionService.NETWORK_AVAILABLE_ACTION);
            serviceIntent.putExtra(ConnectionService.NETWORK_AVAILABLE, connected);
            context.startService(serviceIntent);
        }
    }

    private void debugIntent(Intent intent) {
        Log.d(TAG, "action: " + intent.getAction());
        Log.d(TAG, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key: extras.keySet()) {
                Log.d(TAG, "key [" + key + "]: " +
                        extras.get(key));
            }
        }
        else {
            Log.d(TAG, "no extras");
        }
    }
}
