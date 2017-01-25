package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyProperty;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyProperty extends ProxyProperty<
        SimpleProxyType,
        SimpleProxyCommand,
        SimpleProxyProperty> {

    @Inject
    public SimpleProxyProperty(@Assisted Logger logger,
                               ListenersFactory listenersFactory,
                               ProxyObject.Factory<SimpleProxyCommand> commandFactory) {
        super(logger, listenersFactory, commandFactory);
    }
}
