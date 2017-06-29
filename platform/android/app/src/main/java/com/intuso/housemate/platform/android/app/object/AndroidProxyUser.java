package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyUser;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyUser extends ProxyUser<AndroidProxyCommand, AndroidProxyProperty, AndroidProxyUser> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyUser(Logger logger, String name, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, AndroidObjectFactories factories) {
        super(logger, name, managedCollectionFactory, receiverFactory, factories.command(), factories.property());
    }
}
