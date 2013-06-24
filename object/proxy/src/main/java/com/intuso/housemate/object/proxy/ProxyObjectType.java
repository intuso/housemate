package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;

/**
 */
public abstract class ProxyObjectType<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyObjectType<R, T>>
        extends ProxyType<R, ProxyResources<NoChildrenProxyObjectFactory>, ObjectTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, T> {

    public ProxyObjectType(R resources, ObjectTypeWrappable wrappable) {
        super(resources, null, wrappable);
    }
}
