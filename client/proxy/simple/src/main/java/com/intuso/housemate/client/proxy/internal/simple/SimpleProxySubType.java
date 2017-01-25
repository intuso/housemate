package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxySubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxySubType extends ProxySubType<SimpleProxyType, SimpleProxySubType> {

    @Inject
    public SimpleProxySubType(@Assisted Logger logger,
                              ListenersFactory listenersFactory) {
        super(logger, listenersFactory);
    }
}
