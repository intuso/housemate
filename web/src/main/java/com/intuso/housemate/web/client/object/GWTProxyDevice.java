package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Feature;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyDevice
        extends ProxyDevice<
        GWTProxyCommand,
        GWTProxyProperty,
        GWTProxyValue,
        GWTProxyList<Property.Data, GWTProxyProperty>,
        GWTProxyList<Feature.Data, GWTProxyFeature>,
        GWTProxyDevice> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyDevice(Logger logger,
                          ListenersFactory managedCollectionFactory,
                          GWTGinjector injector,
                          @Assisted Data data) {
        super(logger, managedCollectionFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(Object.Data<?> data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
