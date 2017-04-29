package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyDeviceCombi;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyDeviceCombi extends ProxyDeviceCombi<
        AndroidProxyCommand,
        AndroidProxyList<AndroidProxyCommand>,
        AndroidProxyValue,
        AndroidProxyList<AndroidProxyValue>,
        AndroidProxyList<AndroidProxyProperty>,
        AndroidProxyDeviceCombi> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyDeviceCombi(Logger logger, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, receiverFactory, factories.command(), factories.commands(), factories.value(), factories.values(), factories.properties());
    }
}
