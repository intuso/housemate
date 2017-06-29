package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyDeviceConnected;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyDeviceConnected extends ProxyDeviceConnected<AndroidProxyCommand,
        AndroidProxyList<AndroidProxyCommand>,
        AndroidProxyList<AndroidProxyValue>,
        AndroidProxyDeviceConnected> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyDeviceConnected(Logger logger, String name, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, AndroidObjectFactories factories) {
        super(logger, name, managedCollectionFactory, receiverFactory, factories.command(), factories.commands(), factories.values());
    }
}
