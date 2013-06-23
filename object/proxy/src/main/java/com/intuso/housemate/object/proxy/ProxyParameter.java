package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyParameter<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            P extends ProxyParameter<?, T, P>>
        extends ProxyObject<R, ProxyResources<NoChildrenProxyObjectFactory>, ParameterWrappable, NoChildrenWrappable, NoChildrenProxyObject, P, ParameterListener>
        implements Parameter<T> {

    public ProxyParameter(R resources, ParameterWrappable wrappable) {
        super(resources, null, wrappable);
    }

    @Override
    public final T getType() {
        return (T) getProxyRoot().getTypes().get(getWrappable().getType());
    }
}
