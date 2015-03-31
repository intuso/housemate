package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <TYPE> the type of the type
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<
            TYPE extends ProxyType<?, ?, ?, ?>,
            PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<ParameterData, NoChildrenData, NoChildrenProxyObject, PARAMETER, ParameterListener>
        implements Parameter<TYPE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyParameter(Log log, ListenersFactory listenersFactory, ParameterData data) {
        super(log, listenersFactory, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }
}
