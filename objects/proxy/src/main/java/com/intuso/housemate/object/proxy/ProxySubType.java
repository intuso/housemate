package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory, ?>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            SUB_TYPE extends ProxySubType<?, TYPE, SUB_TYPE>>
        extends ProxyObject<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory, ?>, SubTypeData, NoChildrenData, NoChildrenProxyObject, SUB_TYPE, SubTypeListener>
        implements SubType<TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySubType(RESOURCES resources, SubTypeData data) {
        super(resources, null, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public final TYPE getType() {
        return (TYPE) getProxyRoot().getTypes().get(getData().getType());
    }
}
