package com.intuso.housemate.extension.android.widget.handler;

import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulPowerControl;
import com.intuso.housemate.comms.v1_0.api.TreeLoadInfo;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyDevice;
import com.intuso.housemate.platform.android.app.object.AndroidProxyFeature;
import com.intuso.housemate.platform.android.app.object.AndroidProxyServer;
import com.intuso.housemate.platform.android.app.object.feature.AndroidProxyFeatureFactory;
import com.intuso.housemate.platform.android.app.object.feature.AndroidProxyFeatureImpl;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 06/05/14
* Time: 19:13
* To change this template use File | Settings | File Templates.
*/
public abstract class WidgetHandler<FEATURE extends AndroidProxyFeatureImpl> {

    private static int LOAD_ID = 0;

    public enum Status {
        SERVICE_NOT_READY,
        DEVICE_NOT_LOADED,
        DEVICE_LOAD_FAILED,
        NO_CLIENT,
        NO_DEVICE,
        NO_FEATURE,
        READY
    }

    private final WidgetService widgetService;
    private final AndroidProxyFeatureFactory proxyFeatureFactory;
    private final String clientId;
    private final String deviceId;

    private WidgetService.Status serviceStatus;
    private Status status;
    private AndroidProxyDevice device;
    private FEATURE feature;

    public static WidgetHandler<?> createFeatureWidget(WidgetService widgetService, AndroidProxyFeatureFactory proxyFeatureFactory, String clientId, String deviceId, String featureId) {
        if(featureId.equals(StatefulPowerControl.ID))
            return new StatefulPowerControlWidgetHandler(widgetService, proxyFeatureFactory, clientId, deviceId);
        return null;
    }

    WidgetHandler(WidgetService widgetService, AndroidProxyFeatureFactory proxyFeatureFactory, String clientId, String deviceId) {
        this.widgetService = widgetService;
        this.proxyFeatureFactory = proxyFeatureFactory;
        this.clientId = clientId;
        this.deviceId = deviceId;
    }

    protected WidgetService getWidgetService() {
        return widgetService;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    protected WidgetService.Status getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(WidgetService.Status serviceStatus) {
        this.serviceStatus = serviceStatus;
        if(serviceStatus != WidgetService.Status.LOADED) {
            uninit();
            status = Status.SERVICE_NOT_READY;
            device = null;
            feature = null;
        } else {
            status = Status.DEVICE_NOT_LOADED;
            loadData();
        }
        updateWidget();
    }

    public Status getStatus() {
        return status;
    }

    protected AndroidProxyDevice getDevice() {
        return device;
    }

    protected FEATURE getFeature() {
        return feature;
    }

    private void loadData() {
        final AndroidProxyServer client = widgetService.getRoot().getServers().get(clientId);
        if(client == null)
            status = Status.NO_CLIENT;
        else {
            client.getDevices().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(List<String> errors) {
                    status = Status.DEVICE_LOAD_FAILED;
                    updateWidget();
                }

                @Override
                public void succeeded() {
                    device = client.getDevices().get(deviceId);
                    if (device != null) {
                        AndroidProxyFeature proxyFeature = device.getFeatures().get(getFeatureId());
                        if(proxyFeature != null) {
                            feature = proxyFeatureFactory.getFeatureAs(proxyFeature);
                            init();
                            status = Status.READY;
                            updateWidget();
                        } else
                            status = Status.NO_FEATURE;
                    } else
                        status = Status.NO_DEVICE;
                    updateWidget();
                }
            }, TreeLoadInfo.create(deviceId)));
        }
    }

    protected void init() {}
    protected void uninit() {}

    public abstract String getFeatureId();
    public abstract void handleAction(String action);
    protected abstract void updateWidget();
}
