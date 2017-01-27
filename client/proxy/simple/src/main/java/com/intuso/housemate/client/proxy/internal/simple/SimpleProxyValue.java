package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyValue;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyValue extends ProxyValue<SimpleProxyType, SimpleProxyValue> {

    @Inject
    public SimpleProxyValue(ManagedCollectionFactory managedCollectionFactory,
                            @Assisted Logger logger) {
        super(logger, managedCollectionFactory);
    }
}
