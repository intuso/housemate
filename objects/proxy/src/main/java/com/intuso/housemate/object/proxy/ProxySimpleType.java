package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 */
public abstract class ProxySimpleType<
            TYPE extends ProxySimpleType<TYPE>>
        extends ProxyType<SimpleTypeData, NoChildrenData, NoChildrenProxyObject, TYPE> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySimpleType(Log log, Injector injector, SimpleTypeData data) {
        super(log, injector, data);
    }

    /**
     * Gets the simple type enum value of this type
     * @return the simple type enum value of this type
     */
    public SimpleTypeData.Type getType() {
        return getData().getType();
    }
}
