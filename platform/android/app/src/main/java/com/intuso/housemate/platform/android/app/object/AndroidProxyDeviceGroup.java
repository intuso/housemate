package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyDeviceGroup;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyDeviceGroup extends ProxyDeviceGroup<
        AndroidProxyCommand,
        AndroidProxyList<AndroidProxyCommand>,
        AndroidProxyValue,
        AndroidProxyList<AndroidProxyValue>,
        ProxyDevice<?, ?, ?, ?, ?, ?>,
        AndroidProxyDeviceGroup> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyDeviceGroup(Logger logger, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, AndroidProxyServer server, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, receiverFactory, server, factories.command(), factories.commands(), factories.value(), factories.values());
    }
}
