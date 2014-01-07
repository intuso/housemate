package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<
            TYPE extends ProxyType<?, ?, ?, ?>,
            SUB_TYPE extends ProxySubType<TYPE, SUB_TYPE>>
        extends ProxyObject<SubTypeData, NoChildrenData, NoChildrenProxyObject, SUB_TYPE, SubTypeListener>
        implements SubType<TYPE> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxySubType(Log log, Injector injector, SubTypeData data) {
        super(log, injector, data);
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
