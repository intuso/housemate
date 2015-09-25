package com.intuso.housemate.extension.android.widget.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tomc on 13/05/14.
 */
public class ServiceRestarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, WidgetService.class));
    }
}