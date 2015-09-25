package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyDevice
        extends ProxyDevice<
                    GWTProxyCommand,
                    GWTProxyList<CommandData, GWTProxyCommand>,
                    GWTProxyValue,
                    GWTProxyList<ValueData, GWTProxyValue>,
                    GWTProxyProperty,
                    GWTProxyList<PropertyData, GWTProxyProperty>,
                    GWTProxyFeature,
                    GWTProxyDevice> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyDevice(Log log,
                          ListenersFactory listenersFactory,
                          GWTGinjector injector,
                          @Assisted DeviceData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }

    @Override
    public <F extends GWTProxyFeature> F getFeature(String featureId) {
        return injector.getFeatureFactory().getFeature(featureId, this);
    }
}
