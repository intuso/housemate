package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.SubTypeMapper;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxySubTypeBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.SubType.Data, SubType.Data, SubType.Listener<? super ProxySubTypeBridge>>
        implements SubType<ProxyTypeBridge, ProxySubTypeBridge> {

    @Inject
    protected ProxySubTypeBridge(@Assisted Logger logger,
                                 SubTypeMapper subTypeMapper,
                                 ListenersFactory listenersFactory) {
        super(logger, SubType.Data.class, subTypeMapper, listenersFactory);
    }

    @Override
    public ProxyTypeBridge getType() {
        return null; // todo get the type from somewhere
    }
}
