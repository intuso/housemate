package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.utilities.listener.ListenersFactory;
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
     * @param data {@inheritDoc}
     */
    public ProxySubType(Log log, ListenersFactory listenersFactory, SubTypeData data) {
        super(log, listenersFactory, data);
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
