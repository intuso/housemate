package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;

public class BrokerProxyValue extends BrokerProxyValueBase<ValueData, NoChildrenData,
            NoChildrenBrokerProxyObject, BrokerProxyValue> {
    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyValue(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ValueData data) {
        super(resources, data);
    }
}
