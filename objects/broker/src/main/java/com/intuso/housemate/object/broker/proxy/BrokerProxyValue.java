package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

public class BrokerProxyValue extends BrokerProxyValueBase<ValueWrappable, NoChildrenWrappable,
            NoChildrenBrokerProxyObject, BrokerProxyValue> {
    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyValue(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ValueWrappable data) {
        super(resources, data);
    }
}
