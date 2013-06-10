package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxySubType<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            P extends ProxySubType<?, T, P>>
        extends ProxyObject<R, ProxyResources<NoChildrenProxyObjectFactory>, SubTypeWrappable, NoChildrenWrappable, NoChildrenProxyObject, P, SubTypeListener>
        implements SubType<T> {

    public ProxySubType(R resources, SubTypeWrappable wrappable) {
        super(resources, null, wrappable);
    }

    @Override
    public final T getType() {
        return (T) getProxyRoot().getTypes().get(getWrappable().getType());
    }
}
