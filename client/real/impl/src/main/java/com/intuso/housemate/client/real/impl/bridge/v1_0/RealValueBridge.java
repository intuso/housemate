package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.client.api.bridge.v1_0.ValueMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealValueBridge
        extends RealValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Value.Data, Value.Data, Value.Listener<? super RealValueBridge>, RealValueBridge>
        implements Value<Type.Instances, RealTypeBridge, RealValueBridge> {

    @Inject
    protected RealValueBridge(@Assisted Logger logger,
                              ValueMapper valueMapper,
                              TypeInstancesMapper typeInstancesMapper,
                              ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Value.Data.class, valueMapper, typeInstancesMapper, listenersFactory);
    }
}
