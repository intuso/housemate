package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.ObjectTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxyObjectType<
            TYPE extends ProxyObjectType<TYPE>>
        extends ProxyType<ObjectTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyObjectType(Log log, ListenersFactory listenersFactory, ObjectTypeData data) {
        super(log, listenersFactory, data);
    }
}
