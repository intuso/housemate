package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyDevice extends ProxyDevice<
        AndroidProxyCommand, AndroidProxyList<CommandData, AndroidProxyCommand>,
        AndroidProxyValue, AndroidProxyList<ValueData, AndroidProxyValue>,
        AndroidProxyProperty, AndroidProxyList<PropertyData, AndroidProxyProperty>,
        AndroidProxyFeature, AndroidProxyDevice> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyDevice(Log log, ListenersFactory listenersFactory, DeviceData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }

    @Override
    public <F extends AndroidProxyFeature> F getFeature(String featureId) {
        return factory.createFeature(this, featureId);
    }
}
