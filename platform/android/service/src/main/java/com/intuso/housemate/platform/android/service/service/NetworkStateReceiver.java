package com.intuso.housemate.platform.android.service.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tomc on 02/06/14.
 *
 * Socket client should handle all this by itself anyway, but when it can't conenct it backs off which means it may take
 * a while to connect again when the network becomes available. By handling this here, we can tell it to connect as soon
 * as the network is available.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private boolean networkAvailable = false;

    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED)
                networkAvailable = true;
        }
        Intent serviceIntent = new Intent(context, ConnectionService.class);
        serviceIntent.setAction(ConnectionService.NETWORK_AVAILABLE_ACTION);
        serviceIntent.putExtra(ConnectionService.NETWORK_AVAILABLE, networkAvailable);
        context.startService(serviceIntent);
    }
}
