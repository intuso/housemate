package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.TypeMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class TypeBridge
        extends BridgeObject<com.intuso.housemate.client.v1_0.api.object.Type.Data, Type.Data, Type.Listener<? super TypeBridge>>
        implements Type<TypeBridge> {

    @Inject
    protected TypeBridge(@Assisted Logger logger,
                         TypeMapper typeMapper,
                         ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Type.Data.class, typeMapper, listenersFactory);
    }
}
