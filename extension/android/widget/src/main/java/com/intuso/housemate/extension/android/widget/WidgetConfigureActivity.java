package com.intuso.housemate.extension.android.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.intuso.housemate.api.object.device.feature.StatefulPowerControl;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 28/02/14
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public class WidgetConfigureActivity extends Activity implements ServiceConnection {

    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configure);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, WidgetUpdateService.class);
        startService(intent);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound)
            unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        bound = true;
        int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        WidgetUpdateService.Binder binder = (WidgetUpdateService.Binder)service;
        binder.addWidget(widgetId, "Dog Room Light", StatefulPowerControl.ID);
        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }
}
