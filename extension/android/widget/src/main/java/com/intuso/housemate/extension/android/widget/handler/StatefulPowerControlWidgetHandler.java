package com.intuso.housemate.extension.android.widget.handler;

import android.widget.RemoteViews;
import android.widget.Toast;
import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapper;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.utilities.listener.ManagedCollection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 06/05/14
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class StatefulPowerControlWidgetHandler
        extends WidgetHandler<PowerControl.Stateful>
        implements Command.PerformListener<AndroidProxyCommand> {

    private ManagedCollection.Registration listenerRegistration;

    public StatefulPowerControlWidgetHandler(WidgetService widgetService, ProxyWrapper proxyWrapper, String deviceId) {
        super(widgetService, proxyWrapper, deviceId, PowerControl.Stateful.class);
    }

    @Override
    public String getFeatureId() {
        return PowerControl.Stateful.ID;
    }

    protected void init() {
        listenerRegistration = getFeature().addListener(new PowerControl.Listener() {
            @Override
            public void on(boolean isOn) {
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
                    case DEVICE_NOT_LOADED:
                        views.setTextViewText(R.id.device_label, "Loading device");
                        break;
                    case DEVICE_LOAD_FAILED:
                        views.setTextViewText(R.id.device_label, "Load device failed");
                        break;
                    case NO_DEVICE:
                        views.setTextViewText(R.id.device_label, "Unknown device");
                        break;
                    case NO_FEATURE:
                        views.setTextViewText(R.id.device_label, "Not a powered device");
                        break;
                    case READY:
                        views.setTextViewText(R.id.device_label, getDevice().getName());
                        views.setImageViewResource(R.id.button, getFeature().isOn() ? R.drawable.stateful_power_on : R.drawable.stateful_power_off);
                        // listen for button presses
                        views.setOnClickPendingIntent(R.id.button, getWidgetService().makePendingIntent(this,
                                getFeature() != null && !getFeature().isOn() ? "on" : "off"));
                        break;
                }
                break;
        }

        getWidgetService().updateAppWidget(this, views);
    }

    @Override
    public void handleAction(String action) {
        if("on".equals(action))
            getFeature().turnOn();
        else if("off".equals(action))
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
