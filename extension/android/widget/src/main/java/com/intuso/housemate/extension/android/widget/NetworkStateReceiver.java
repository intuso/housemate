package com.intuso.housemate.extension.android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tomc on 02/06/14.
 *
 * Socket client should handle all this by itself anyway, but when it can't connect it backs off which means it may take
 * a while to connect again when the network becomes available. By handling this here, we can tell it to connect as soon
 * as the network is available.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo ni = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.setAction(WidgetService.NETWORK_AVAILABLE_ACTION);
            serviceIntent.putExtra(WidgetService.NETWORK_AVAILABLE, ni != null && ni.isConnectedOrConnecting());
            context.startService(serviceIntent);
        }
    }
}
