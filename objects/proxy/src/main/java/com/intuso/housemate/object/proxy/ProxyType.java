package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.utilities.log.Log;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <TYPE> the type of the type
 */
public abstract class ProxyType<
            DATA extends TypeData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            TYPE extends ProxyType<DATA, CHILD_DATA, CHILD, TYPE>>
        extends ProxyObject<DATA, CHILD_DATA, CHILD, TYPE, TypeListener>
        implements Type {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyType(Log log, Injector injector, DATA data) {
        super(log, injector, data);
    }
}
