package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyList;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyList<ELEMENT extends ProxyObject<?, ?>> extends ProxyList<ELEMENT, SimpleProxyList<ELEMENT>> {

    @Inject
    public SimpleProxyList(@Assisted Logger logger,
                           ManagedCollectionFactory managedCollectionFactory,
                           ProxyObject.Factory<ELEMENT> elementFactory) {
        super(logger, managedCollectionFactory, elementFactory);
    }
}
