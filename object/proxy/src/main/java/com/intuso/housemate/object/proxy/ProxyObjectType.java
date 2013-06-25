package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 */
public abstract class ProxyObjectType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory>,
            TYPE extends ProxyObjectType<RESOURCES, TYPE>>
        extends ProxyType<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory>, ObjectTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyObjectType(RESOURCES resources, ObjectTypeWrappable wrappable) {
        super(resources, null, wrappable);
    }
}
