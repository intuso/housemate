package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            SUB_TYPE extends ProxySubType<?, TYPE, SUB_TYPE>>
        extends ProxyObject<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory>, SubTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, SUB_TYPE, SubTypeListener>
        implements SubType<TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySubType(RESOURCES resources, SubTypeWrappable data) {
        super(resources, null, data);
    }

    @Override
    public final TYPE getType() {
        return (TYPE) getProxyRoot().getTypes().get(getData().getType());
    }
}
