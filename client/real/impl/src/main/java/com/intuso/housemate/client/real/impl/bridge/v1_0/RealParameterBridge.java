package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ParameterMapper;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealParameterBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Parameter.Data, Parameter.Data, Parameter.Listener<? super RealParameterBridge>>
        implements Parameter<RealTypeBridge, RealParameterBridge> {

    @Inject
    protected RealParameterBridge(@Assisted Logger logger,
                                  ParameterMapper parameterMapper,
                                  ManagedCollectionFactory managedCollectionFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Parameter.Data.class, parameterMapper, managedCollectionFactory);
    }

    @Override
    public RealTypeBridge getType() {
        return null; // todo get the type from somewhere
    }
}
