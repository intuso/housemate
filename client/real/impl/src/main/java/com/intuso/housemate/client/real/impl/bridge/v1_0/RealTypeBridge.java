package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.view.TypeView;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealTypeBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Type.Data, Type.Data, Type.Listener<? super RealTypeBridge>, TypeView>
        implements Type<RealTypeBridge> {

    @Inject
    protected RealTypeBridge(@Assisted Logger logger,
                             TypeMapper typeMapper,
                             ManagedCollectionFactory managedCollectionFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Type.Data.class, typeMapper, managedCollectionFactory);
    }

    @Override
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        return null;
    }
}
