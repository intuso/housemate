package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxySimpleType<R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxySimpleType<R, T>>
        extends ProxyType<R, ProxyResources<NoChildrenProxyObjectFactory>, SimpleTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, T> {
    public ProxySimpleType(R resources, SimpleTypeWrappable wrappable) {
        super(resources, null, wrappable);
    }

    public SimpleTypeWrappable.Type getType() {
        return getWrappable().getType();
    }
}
