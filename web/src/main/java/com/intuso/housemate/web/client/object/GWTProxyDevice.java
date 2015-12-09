package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyDevice
        extends ProxyDevice<
        GWTProxyCommand,
        GWTProxyProperty,
        GWTProxyValue,
        GWTProxyList<PropertyData, GWTProxyProperty>,
        GWTProxyList<FeatureData, GWTProxyFeature>,
        GWTProxyDevice> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyDevice(Logger logger,
                          ListenersFactory listenersFactory,
                          GWTGinjector injector,
                          @Assisted DeviceData data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
