package com.intuso.housemate.extension.android.widget.handler;

import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.housemate.client.v1_0.proxy.api.annotation.ProxyWrapper;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyDevice;
import com.intuso.housemate.platform.android.app.object.AndroidProxyFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 06/05/14
* Time: 19:13
* To change this template use File | Settings | File Templates.
*/
public abstract class WidgetHandler<FEATURE> {

    private final Logger logger = LoggerFactory.getLogger(WidgetHandler.class);

    private static int LOAD_ID = 0;

    public enum Status {
        SERVICE_NOT_READY,
        DEVICE_NOT_LOADED,
        DEVICE_LOAD_FAILED,
        NO_DEVICE,
        NO_FEATURE,
        READY
    }

    private final WidgetService widgetService;
    private final ProxyWrapper proxyWrapper;
    private final String deviceId;
    private final Class<FEATURE> featureClass;

    private WidgetService.Status serviceStatus;
    private Status status;
    private AndroidProxyDevice device;
    private FEATURE feature;

    public static WidgetHandler<?> createFeatureWidget(WidgetService widgetService, ProxyWrapper proxyFeatureFactory, String deviceId, String featureId) {
        if(featureId.equals(PowerControl.ID))
            return new StatefulPowerControlWidgetHandler(widgetService, proxyFeatureFactory, deviceId);
        return null;
    }

    WidgetHandler(WidgetService widgetService, ProxyWrapper proxyWrapper, String deviceId, Class<FEATURE> featureClass) {
        this.widgetService = widgetService;
        this.proxyWrapper = proxyWrapper;
        this.deviceId = deviceId;
        this.featureClass = featureClass;
    }

    protected WidgetService getWidgetService() {
        return widgetService;
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
        device = widgetService.getServer().getDevices().get(deviceId);
        if (device != null) {
            AndroidProxyFeature proxyFeature = device.getFeatures().get(getFeatureId());
            if(proxyFeature != null) {
                feature = proxyWrapper.build(logger, proxyFeature, featureClass, "", 3000L);
                init();
                status = Status.READY;
                updateWidget();
            } else
                status = Status.NO_FEATURE;
        } else
            status = Status.NO_DEVICE;
        updateWidget();
    }

    protected void init() {}
    protected void uninit() {}

    public abstract String getFeatureId();
    public abstract void handleAction(String action);
    protected abstract void updateWidget();
}
