package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyFeature;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyFeature extends ProxyFeature<SimpleProxyCommand,
        SimpleProxyList<SimpleProxyCommand>,
        SimpleProxyValue,
        SimpleProxyList<SimpleProxyValue>,
        SimpleProxyProperty,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxyFeature> {

    @Inject
    public SimpleProxyFeature(@Assisted Logger logger,
                              ManagedCollectionFactory managedCollectionFactory,
                              ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                              ProxyObject.Factory<SimpleProxyList<SimpleProxyCommand>> commandsFactory,
                              ProxyObject.Factory<SimpleProxyValue> valueFactory,
                              ProxyObject.Factory<SimpleProxyList<SimpleProxyValue>> valuesFactory,
                              ProxyObject.Factory<SimpleProxyProperty> propertyFactory,
                              ProxyObject.Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory) {
        super(logger, managedCollectionFactory, commandFactory, commandsFactory, valueFactory, valuesFactory, propertyFactory, propertiesFactory);
    }
}