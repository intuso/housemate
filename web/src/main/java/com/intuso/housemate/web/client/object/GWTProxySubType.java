package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.NoChildrenProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxySubType;
import com.intuso.housemate.client.v1_0.data.api.payload.NoChildrenData;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxySubType extends ProxySubType<GWTProxySubType> {

    @Inject
    public GWTProxySubType(Logger logger,
                           ListenersFactory managedCollectionFactory,
                           @Assisted Data data) {
        super(logger, managedCollectionFactory, data);
    }

    @Override
    protected NoChildrenProxyObject createChild(NoChildrenData noChildrenData) {
        return null;
    }
}
