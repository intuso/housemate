package com.intuso.housemate.extension.android.widget.handler;

import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.housemate.client.v1_0.proxy.annotation.ProxyWrapper;
import com.intuso.housemate.extension.android.widget.service.WidgetService;
import com.intuso.housemate.platform.android.app.object.AndroidProxySystem;
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
        NO_DEVICE,
        READY
    }

    private final WidgetService widgetService;
    private final ProxyWrapper proxyWrapper;
    private final String deviceId;
    private final Class<FEATURE> featureClass;

    private WidgetService.Status serviceStatus;
    private Status status;
    private AndroidProxySystem device;
    private FEATURE feature;

    public static WidgetHandler<?> createFeatureWidget(WidgetService widgetService, ProxyWrapper proxyFeatureFactory, String deviceId, String featureId) {
        if(featureId.equals(Power.ID))
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
            status = Status.NO_DEVICE;
            loadData();
        }
        updateWidget();
    }

    public Status getStatus() {
        return status;
    }

    protected AndroidProxySystem getDevice() {
        return device;
    }

    protected FEATURE getFeature() {
        return feature;
    }

    private void loadData() {
        device = widgetService.getServer().getSystems().get(deviceId);
        if (device != null) {
            feature = proxyWrapper.build(device, featureClass, "", 3000L);
            init();
            status = Status.READY;
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
