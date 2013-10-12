package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.SimpleTypeData;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 */
public abstract class ProxySimpleType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory, ?>,
            TYPE extends ProxySimpleType<RESOURCES, TYPE>>
        extends ProxyType<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory, ?>, SimpleTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySimpleType(RESOURCES resources, SimpleTypeData data) {
        super(resources, null, data);
    }

    /**
     * Gets the simple type enum value of this type
     * @return the simple type enum value of this type
     */
    public SimpleTypeData.Type getType() {
        return getData().getType();
    }
}
