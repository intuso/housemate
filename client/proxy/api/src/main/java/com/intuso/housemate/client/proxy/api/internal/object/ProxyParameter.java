package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<TYPE extends ProxyType<?>,
            PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<Parameter.Data, Parameter.Listener<? super PARAMETER>>
        implements Parameter<TYPE, PARAMETER> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyParameter(Logger logger, ListenersFactory listenersFactory) {
        super(logger, Parameter.Data.class, listenersFactory);
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }
}
