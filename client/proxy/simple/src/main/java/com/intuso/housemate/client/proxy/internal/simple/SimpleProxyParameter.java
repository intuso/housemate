package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyParameter;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyParameter extends ProxyParameter<SimpleProxyType, SimpleProxyParameter> {

    @Inject
    public SimpleProxyParameter(@Assisted Logger logger,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(logger, managedCollectionFactory);
    }
}
