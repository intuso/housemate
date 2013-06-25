package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 */
public abstract class ProxySimpleType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory>,
            TYPE extends ProxySimpleType<RESOURCES, TYPE>>
        extends ProxyType<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory>, SimpleTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxySimpleType(RESOURCES resources, SimpleTypeWrappable wrappable) {
        super(resources, null, wrappable);
    }

    /**
     * Gets the simple type enum value of this type
     * @return the simple type enum value of this type
     */
    public SimpleTypeWrappable.Type getType() {
        return getWrappable().getType();
    }
}
