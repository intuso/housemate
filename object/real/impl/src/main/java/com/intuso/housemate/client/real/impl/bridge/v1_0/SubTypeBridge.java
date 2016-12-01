package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class SubTypeBridge
        extends BridgeObject<com.intuso.housemate.client.v1_0.api.object.SubType.Data, SubType.Data, SubType.Listener<? super SubTypeBridge>>
        implements SubType<TypeBridge, SubTypeBridge> {

    @Inject
    protected SubTypeBridge(@Assisted Logger logger,
                            Function<com.intuso.housemate.client.v1_0.api.object.SubType.Data, SubType.Data> dataMapper,
                            ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.SubType.Data.class, dataMapper, listenersFactory);
    }

    @Override
    public TypeBridge getType() {
        return null; // todo get the type from somewhere
    }

    public interface Factory {
        SubTypeBridge create(Logger logger);
    }
}
