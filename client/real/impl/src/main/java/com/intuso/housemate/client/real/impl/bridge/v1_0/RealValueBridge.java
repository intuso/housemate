package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.ValueMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.view.ValueView;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealValueBridge
        extends RealValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Value.Data, Value.Data, Value.Listener<? super RealValueBridge>, ValueView, RealValueBridge>
        implements Value<Type.Instances, RealTypeBridge, RealValueBridge> {

    @Inject
    protected RealValueBridge(@Assisted Logger logger,
                              ValueMapper valueMapper,
                              TypeInstancesMapper typeInstancesMapper,
                              ManagedCollectionFactory managedCollectionFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Value.Data.class, valueMapper, typeInstancesMapper, managedCollectionFactory);
    }

    @Override
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        return null;
    }
}
