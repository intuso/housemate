package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxySystem;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxySystem extends ProxySystem<
        SimpleProxyValue,
        SimpleProxyCommand,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxySystem> {

    @Inject
    public SimpleProxySystem(@Assisted Logger logger,
                             ManagedCollectionFactory managedCollectionFactory,
                             Factory<SimpleProxyCommand> commandFactory,
                             Factory<SimpleProxyValue> valueFactory,
                             Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory) {
        super(logger, managedCollectionFactory, valueFactory, commandFactory, propertiesFactory);
    }
}
