package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.SubTypeMapper;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealSubTypeBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.SubType.Data, SubType.Data, SubType.Listener<? super RealSubTypeBridge>>
        implements SubType<RealTypeBridge, RealSubTypeBridge> {

    @Inject
    protected RealSubTypeBridge(@Assisted Logger logger,
                                SubTypeMapper subTypeMapper,
                                ManagedCollectionFactory managedCollectionFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.SubType.Data.class, subTypeMapper, managedCollectionFactory);
    }

    @Override
    public RealTypeBridge getType() {
        return null; // todo get the type from somewhere
    }
}
