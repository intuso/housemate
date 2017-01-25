package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyCommand;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyCommand extends ProxyCommand<
        SimpleProxyValue,
        SimpleProxyList<SimpleProxyParameter>,
        SimpleProxyCommand> {

    @Inject
    public SimpleProxyCommand(@Assisted Logger logger,
                              ListenersFactory listenersFactory,
                              ProxyObject.Factory<SimpleProxyValue> valueFactory,
                              ProxyObject.Factory<SimpleProxyList<SimpleProxyParameter>> parametersFactory) {
        super(logger, listenersFactory, valueFactory, parametersFactory);
    }
}
