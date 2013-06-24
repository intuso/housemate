package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 */
public class BrokerProxyType
        extends BrokerProxyObject<TypeWrappable<HousemateObjectWrappable<?>>,
            HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>, BrokerProxyType, TypeListener>
        implements Type {
    public BrokerProxyType(BrokerProxyResources<BrokerProxyFactory.All> resources, TypeWrappable wrappable) {
        super(resources, wrappable);
    }
}
