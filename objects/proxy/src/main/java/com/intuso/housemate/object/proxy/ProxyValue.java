package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public class ProxyValue<
            TYPE extends ProxyType<?, ?, ?, ?>,
            VALUE extends ProxyValue<TYPE, VALUE>>
        extends ProxyValueBase<ValueData, NoChildrenData, NoChildrenProxyObject, TYPE, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValue(Log log, Injector injector, ValueData data) {
        super(log, injector, data);
    }
}
