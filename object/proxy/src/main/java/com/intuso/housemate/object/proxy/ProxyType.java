package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <TYPE> the type of the type
 */
public abstract class ProxyType<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CHILD_DATA, CHILD>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            DATA extends TypeWrappable<CHILD_DATA>,
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends ProxyObject<?, ?, ? extends CHILD_DATA, ?, ?, ?, ?>,
            TYPE extends ProxyType<RESOURCES, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, TYPE>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, TYPE, TypeListener>
        implements Type {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyType(RESOURCES resources, CHILD_RESOURCES childResources, DATA wrappable) {
        super(resources, childResources, wrappable);
    }
}
