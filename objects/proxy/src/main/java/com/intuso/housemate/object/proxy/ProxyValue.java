package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public class ProxyValue<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            VALUE extends ProxyValue<RESOURCES, TYPE, VALUE>>
        extends ProxyValueBase<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory>, ValueWrappable, NoChildrenWrappable, NoChildrenProxyObject, TYPE, VALUE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValue(RESOURCES resources, ValueWrappable data) {
        super(resources, null, data);
    }
}
