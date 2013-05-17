package com.intuso.housemate.proxy;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.type.ObjectTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyObjectType<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyObjectType<R, T>>
        extends ProxyType<R, ProxyResources<NoChildrenProxyObjectFactory>, ObjectTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, T> {

    public ProxyObjectType(R resources, ObjectTypeWrappable wrappable) {
        super(resources, null, wrappable);
    }
}
