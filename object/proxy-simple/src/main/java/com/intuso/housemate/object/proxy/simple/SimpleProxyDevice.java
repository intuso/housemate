package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyDevice extends ProxyDevice<
        SimpleProxyCommand,
        SimpleProxyList<CommandData, SimpleProxyCommand>, SimpleProxyValue,
        SimpleProxyList<ValueData, SimpleProxyValue>,
        SimpleProxyProperty,
        SimpleProxyList<PropertyData, SimpleProxyProperty>,
            SimpleProxyFeature,
        SimpleProxyDevice> {

    private final Injector injector;

    @Inject
    public SimpleProxyDevice(Log log,
                             ListenersFactory listenersFactory,
                             Injector injector,
                             @Assisted DeviceData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }

    @Override
    public <F extends SimpleProxyFeature> F getFeature(String featureId) {
        return injector.getInstance(new Key<ProxyFeatureFactory<SimpleProxyFeature, SimpleProxyDevice>>() {}).getFeature(featureId, getThis());
    }
}
