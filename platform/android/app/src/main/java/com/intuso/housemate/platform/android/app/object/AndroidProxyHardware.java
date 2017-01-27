package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyHardware;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyHardware extends ProxyHardware<
        AndroidProxyCommand,
        AndroidProxyList<AndroidProxyCommand>,
        AndroidProxyValue,
        AndroidProxyList<AndroidProxyValue>,
        AndroidProxyProperty,
        AndroidProxyList<AndroidProxyProperty>,
        AndroidProxyHardware> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyHardware(Logger logger, ManagedCollectionFactory managedCollectionFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, factories.command(), factories.commands(), factories.value(),
                factories.values(), factories.property(), factories.properties());
    }
}
