package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;

/**
 * @param <RESOURCES> the type of the resources
 * @param <TYPE> the type of the type
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<
            RESOURCES extends ProxyResources<NoChildrenProxyObjectFactory, ?>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            PARAMETER extends ProxyParameter<?, TYPE, PARAMETER>>
        extends ProxyObject<RESOURCES, ProxyResources<NoChildrenProxyObjectFactory, ?>, ParameterData, NoChildrenData, NoChildrenProxyObject, PARAMETER, ParameterListener>
        implements Parameter<TYPE> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyParameter(RESOURCES resources, ParameterData data) {
        super(resources, null, data);
    }

    @Override
    public final TYPE getType() {
        return (TYPE) getProxyRoot().getTypes().get(getData().getType());
    }
}
