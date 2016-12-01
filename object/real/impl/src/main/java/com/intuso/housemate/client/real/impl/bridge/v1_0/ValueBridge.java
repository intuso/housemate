package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ValueBridge
        extends ValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Value.Data, Value.Data, Value.Listener<? super ValueBridge>, ValueBridge>
        implements Value<Type.Instances, TypeBridge, ValueBridge> {

    @Inject
    protected ValueBridge(@Assisted Logger logger,
                          Function<com.intuso.housemate.client.v1_0.api.object.Value.Data, Value.Data> dataMapper,
                          Function<com.intuso.housemate.client.v1_0.api.object.Type.Instances, Type.Instances> typeInstancesMapper,
                          ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Value.Data.class, dataMapper, typeInstancesMapper, listenersFactory);
    }

    public interface Factory {
        ValueBridge create(Logger logger);
    }
}
