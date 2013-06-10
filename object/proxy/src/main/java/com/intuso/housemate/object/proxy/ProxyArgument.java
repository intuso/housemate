package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.argument.Argument;
import com.intuso.housemate.api.object.argument.ArgumentListener;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyArgument<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            P extends ProxyArgument<?, T, P>>
        extends ProxyObject<R, ProxyResources<NoChildrenProxyObjectFactory>, ArgumentWrappable, NoChildrenWrappable, NoChildrenProxyObject, P, ArgumentListener>
        implements Argument<T> {

    public ProxyArgument(R resources, ArgumentWrappable wrappable) {
        super(resources, null, wrappable);
    }

    @Override
    public final T getType() {
        return (T) getProxyRoot().getTypes().get(getWrappable().getType());
    }
}
