package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.ObjectTypeData;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 */
public abstract class ProxyObjectType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory, ?>,
            TYPE extends ProxyObjectType<RESOURCES, TYPE>>
        extends ProxyType<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory, ?>, ObjectTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyObjectType(RESOURCES resources, ObjectTypeData data) {
        super(resources, null, data);
    }
}
