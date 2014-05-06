package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValue<
            TYPE extends ProxyType<?, ?, ?, ?>,
            VALUE extends ProxyValue<TYPE, VALUE>>
        extends ProxyValueBase<ValueData, NoChildrenData, NoChildrenProxyObject, TYPE, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValue(Log log, ListenersFactory listenersFactory, ValueData data) {
        super(log, listenersFactory, data);
    }
}
