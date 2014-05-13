package com.intuso.housemate.extension.android.widget.handler;

import android.widget.RemoteViews;
import android.widget.Toast;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.device.feature.StatefulPowerControl;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.extension.android.widget.R;
import com.intuso.housemate.extension.android.widget.WidgetService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.housemate.platform.android.app.object.AndroidProxyValue;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 06/05/14
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class StatefulPowerControlWidgetHandler
        extends WidgetHandler<StatefulPowerControl<AndroidProxyCommand, AndroidProxyValue>>
        implements CommandPerformListener<AndroidProxyCommand> {

    private ListenerRegistration listenerRegistration;

    public StatefulPowerControlWidgetHandler(WidgetService widgetService, String deviceId) {
        super(widgetService, deviceId);
    }

    @Override
    public String getFeatureId() {
        return StatefulPowerControl.ID;
    }

    protected void init() {
        listenerRegistration = getFeature().getIsOnValue().addObjectListener(new ValueListener<AndroidProxyValue>() {
            @Override
            public void valueChanging(AndroidProxyValue value) {
                // do nothing
            }

            @Override
            public void valueChanged(AndroidProxyValue value) {
                updateWidget();
            }
        });
    }

    protected void uninit() {
        if(listenerRegistration != null) {
            listenerRegistration.removeListener();
            listenerRegistration = null;
        }
    }

    @Override
    public void updateWidget() {

        // make the remote views
        RemoteViews views = new RemoteViews(getWidgetService().getApplicationContext().getPackageName(), R.layout.stateful_power_widget);

        // listen for button presses
        views.setOnClickPendingIntent(R.id.button, getWidgetService().makePendingIntent(this,
                getFeature() != null && !getFeature().isOn() ? StatefulPowerControl.ON_COMMAND : StatefulPowerControl.OFF_COMMAND));

        switch (getServiceStatus()) {
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
                    case NO_DEVICE:
                        views.setTextViewText(R.id.device_label, "Unknown device");
                        break;
                    case NO_FEATURE:
                        views.setTextViewText(R.id.device_label, "Not a powered device");
                        break;
                    case FEATURE_NOT_LOADED:
                        views.setTextViewText(R.id.device_label, "Loading feature");
                        break;
                    case READY:
                        views.setTextViewText(R.id.device_label, getDevice().getName());
                        views.setImageViewResource(R.id.button, getFeature().isOn() ? R.drawable.stateful_power_on : R.drawable.stateful_power_off);
                        break;
                }
                break;
        }

        getWidgetService().updateAppWidget(this, views);
    }

    @Override
    public void handleAction(String action) {
        if(StatefulPowerControl.ON_COMMAND.equals(action))
            getFeature().getOnCommand().perform(this);
        else if(StatefulPowerControl.OFF_COMMAND.equals(action))
            getFeature().getOffCommand().perform(this);
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
