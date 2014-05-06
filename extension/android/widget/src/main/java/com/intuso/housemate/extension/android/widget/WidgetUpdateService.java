package com.intuso.housemate.extension.android.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.device.feature.FeatureLoadedListener;
import com.intuso.housemate.object.proxy.simple.ProxyClientHelper;
import com.intuso.housemate.platform.android.app.HousemateService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyDevice;
import com.intuso.housemate.platform.android.app.object.AndroidProxyFeature;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;
import com.intuso.housemate.platform.android.app.object.AndroidProxyValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 28/02/14
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class WidgetUpdateService extends HousemateService implements LoadManager.Callback {

    private final static ApplicationDetails APPLICATION_DETAILS
            = new ApplicationDetails(WidgetUpdateService.class.getName(), "Android Widgets", "Android Widgets");

    private final Binder binder = new Binder();

    private final List<WidgetHandler> widgetHandlers = Lists.newArrayList();

    private ProxyClientHelper<AndroidProxyRoot> clientHelper;
    private AppWidgetManager appWidgetManager;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clientHelper = ProxyClientHelper.newClientHelper(getLog(),
                new AndroidProxyRoot(getLog(), getListenersFactory(), getProperties(), getRouter()), getRouter());
        clientHelper.applicationDetails(APPLICATION_DETAILS)
                .load(Root.DEVICES_ID)
                .callback(this)
                .start();
        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        if(clientHelper != null) {
            clientHelper.stop();
            clientHelper = null;
        }
        super.onDestroy();
    }

    @Override
    public void failed(HousemateObject.TreeLoadInfo path) {
        // do nothing
    }

    @Override
    public void allLoaded() {
        for(WidgetHandler widgetHandler : widgetHandlers)
            widgetHandler.loadData();
    }

    private void addWidgetHandler(WidgetHandler widgetHandler) {
        widgetHandlers.add(widgetHandler);
        if(clientHelper.getRoot().getApplicationInstanceStatus() == ApplicationInstanceStatus.Allowed)
            widgetHandler.loadData();
    }

    public class Binder extends android.os.Binder {
        public void addWidget(int widgetId, String deviceId, String featureId) {
            addWidgetHandler(new WidgetHandler(widgetId, deviceId, featureId));
        }
    }

    private class WidgetHandler implements
            LoadManager.Callback,
            FeatureLoadedListener<AndroidProxyDevice, AndroidProxyFeature>,
            ValueListener<AndroidProxyValue> {

        private final int widgetId;
        private final String deviceId;
        private final String featureId;

        private WidgetHandler(int widgetId, String deviceId, String featureId) {
            this.widgetId = widgetId;
            this.deviceId = deviceId;
            this.featureId = featureId;
            updateWidget();
        }

        private void loadData() {
            clientHelper.getRoot().getDevices().load(new LoadManager(this, "androidWidgetServiceInitialLoad", HousemateObject.TreeLoadInfo.create(deviceId)));
        }

        @Override
        public void failed(HousemateObject.TreeLoadInfo path) {
            updateWidget();
        }

        @Override
        public void allLoaded() {
            AndroidProxyDevice device = clientHelper.getRoot().getDevices().get(deviceId);
            if(device != null) {
                AndroidProxyFeature feature = device.getFeature(featureId);
                if(feature != null)
                    feature.load(this);
            }
        }

        @Override
        public void featureLoaded(AndroidProxyDevice device, AndroidProxyFeature feature) {
            updateWidget();
            if(feature instanceof AndroidProxyFeature.StatefulPowerControl)
                ((AndroidProxyFeature.StatefulPowerControl)feature).getIsOnValue().addObjectListener(this);
        }

        private void updateWidget() {
            RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
            if(clientHelper.getRoot() == null)
                views.setTextViewText(R.id.status_label, "NR");
            else if(clientHelper.getRoot().getDevices() == null)
                views.setTextViewText(R.id.status_label, "NDs");
            else {
                AndroidProxyDevice device = clientHelper.getRoot().getDevices().get(deviceId);
                if(device == null)
                    views.setTextViewText(R.id.status_label, "ND");
                else {
                    AndroidProxyFeature.StatefulPowerControl feature = device.getFeature(featureId);
                    if(feature == null)
                        views.setTextViewText(R.id.status_label, "NF");
                    else
                        views.setTextViewText(R.id.status_label, feature.isOn() ? "On" : "Off");
                }
                appWidgetManager.updateAppWidget(widgetId, views);
            }
        }

        @Override
        public void valueChanging(AndroidProxyValue value) {
            // do nothing
        }

        @Override
        public void valueChanged(AndroidProxyValue value) {
            updateWidget();
        }
    }
}
