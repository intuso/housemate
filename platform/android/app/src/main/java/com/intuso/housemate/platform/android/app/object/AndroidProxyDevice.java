package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyDevice;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyDevice extends ProxyDevice<
        AndroidProxyValue,
        AndroidProxyCommand,
        AndroidProxyList<AndroidProxyProperty>,
        AndroidProxyDevice> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyDevice(Logger logger, ManagedCollectionFactory managedCollectionFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, factories.value(), factories.command(), factories.properties());
    }
}
