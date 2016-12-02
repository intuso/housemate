package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.ParameterMapper;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ParameterBridge
        extends BridgeObject<com.intuso.housemate.client.v1_0.api.object.Parameter.Data, Parameter.Data, Parameter.Listener<? super ParameterBridge>>
        implements Parameter<TypeBridge, ParameterBridge> {

    @Inject
    protected ParameterBridge(@Assisted Logger logger,
                              ParameterMapper parameterMapper,
                              ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Parameter.Data.class, parameterMapper, listenersFactory);
    }

    @Override
    public TypeBridge getType() {
        return null; // todo get the type from somewhere
    }
}
