package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public class ProxyValue<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            VALUE extends ProxyValue<RESOURCES, TYPE, VALUE>>
        extends ProxyValueBase<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory>, ValueData, NoChildrenData, NoChildrenProxyObject, TYPE, VALUE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValue(RESOURCES resources, ValueData data) {
        super(resources, null, data);
    }
}
