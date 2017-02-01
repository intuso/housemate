package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyHardware;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyHardware extends ProxyHardware<
        SimpleProxyCommand,
        SimpleProxyList<SimpleProxyCommand>,
        SimpleProxyValue,
        SimpleProxyList<SimpleProxyValue>,
        SimpleProxyProperty,
        SimpleProxyList<SimpleProxyProperty>,
        SimpleProxyList<SimpleProxyConnectedDevice>,
        SimpleProxyHardware> {

    @Inject
    public SimpleProxyHardware(@Assisted Logger logger,
                               ManagedCollectionFactory managedCollectionFactory,
                               ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                               ProxyObject.Factory<SimpleProxyList<SimpleProxyCommand>> commandsFactory,
                               ProxyObject.Factory<SimpleProxyValue> valueFactory,
                               ProxyObject.Factory<SimpleProxyList<SimpleProxyValue>> valuesFactory,
                               ProxyObject.Factory<SimpleProxyProperty> propertyFactory,
                               ProxyObject.Factory<SimpleProxyList<SimpleProxyProperty>> propertiesFactory,
                               ProxyObject.Factory<SimpleProxyList<SimpleProxyConnectedDevice>> devicesFactory) {
        super(logger, managedCollectionFactory, commandFactory, commandsFactory, valueFactory, valuesFactory, propertyFactory, propertiesFactory, devicesFactory);
    }
}
