package com.intuso.housemate.extension.android.widget.handler;

import android.widget.RemoteViews;
import android.widget.Toast;
import com.intuso.housemate.client.v1_0.api.api.Power;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapper;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 06/05/14
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class StatefulPowerControlWidgetHandler
        extends WidgetHandler<Power>
        implements Command.PerformListener<AndroidProxyCommand> {

    private ManagedCollection.Registration listenerRegistration;
    private Boolean isOn = null;

    public StatefulPowerControlWidgetHandler(WidgetService widgetService, ProxyWrapper proxyWrapper, String deviceId) {
        super(widgetService, proxyWrapper, deviceId, Power.class);
    }

    @Override
    public String getFeatureId() {
        return Power.ID;
    }

    protected void init() {
        listenerRegistration = getFeature().addListener(new Power.Listener() {
            @Override
            public void on(Boolean isOn) {
                StatefulPowerControlWidgetHandler.this.isOn = true;
                updateWidget();
            }
        });
    }

    protected void uninit() {
        if(listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    @Override
    public void updateWidget() {

        // make the remote views
        RemoteViews views = new RemoteViews(getWidgetService().getApplicationContext().getPackageName(), R.layout.stateful_power_widget);
        switch (getServiceStatus()) {
            case SERVICE_STOPPED:
                views.setTextViewText(R.id.device_label, "Widget service stopped");
                break;
            case NO_NETWORK:
                views.setTextViewText(R.id.device_label, "No network");
                break;
            case NOT_CONNECTED:
                views.setTextViewText(R.id.device_label, "Not connected");
                break;
            case NOT_ALLOWED:
                views.setTextViewText(R.id.device_label, "No Access");
                break;
            case NOT_LOADED:
                views.setTextViewText(R.id.device_label, "Loading device list");
                break;
            case LOADED:
                switch (getStatus()) {
                    case NO_DEVICE:
                        views.setTextViewText(R.id.device_label, "Unknown device");
                        break;
                    case READY:
                        views.setTextViewText(R.id.device_label, getDevice().getName());
                        views.setImageViewResource(R.id.button, isOn ? R.drawable.stateful_power_on : R.drawable.stateful_power_off);
                        // listen for button presses
                        views.setOnClickPendingIntent(R.id.button, getWidgetService().makePendingIntent(this, isOn ? "off" : "on"));
                        break;
                }
                break;
        }

        getWidgetService().updateAppWidget(this, views);
    }

    @Override
    public void handleAction(String action) {
        if(isOn)
            getFeature().turnOn();
        else
            getFeature().turnOff();
    }

    @Override
    public void commandStarted(AndroidProxyCommand command) {
        // do nothing
    }

    @Override
    public void commandFinished(AndroidProxyCommand command) {
        // do nothing
    }

    @Override
    public void commandFailed(AndroidProxyCommand command, String error) {
        Toast.makeText(getWidgetService().getApplicationContext(), "Command failed: " + error, Toast.LENGTH_SHORT).show();
    }
}
