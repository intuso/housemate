package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.NoChildrenProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxySubType;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxySubType extends ProxySubType<GWTProxySubType> {

    @Inject
    public GWTProxySubType(Log log,
                           ListenersFactory listenersFactory,
                           @Assisted SubTypeData data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
