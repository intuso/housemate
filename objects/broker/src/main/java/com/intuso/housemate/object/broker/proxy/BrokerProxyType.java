package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeListener;

public class BrokerProxyType
        extends BrokerProxyObject<TypeData<HousemateData<?>>,
        HousemateData<?>, BrokerProxyObject<?, ?, ?, ?, ?>, BrokerProxyType, TypeListener>
        implements Type {
    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyType(BrokerProxyResources<BrokerProxyFactory.All> resources, TypeData data) {
        super(resources, data);
    }
}
