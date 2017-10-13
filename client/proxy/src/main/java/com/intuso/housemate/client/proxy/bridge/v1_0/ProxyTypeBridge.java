package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.view.TypeView;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyTypeBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Type.Data, Type.Data, Type.Listener<? super ProxyTypeBridge>, TypeView>
        implements Type<ProxyTypeBridge> {

    @Inject
    protected ProxyTypeBridge(@Assisted Logger logger,
                              TypeMapper typeMapper,
                              ManagedCollectionFactory managedCollectionFactory,
                              com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                              Sender.Factory v1_0SenderFactory) {
        super(logger, Type.Data.class, typeMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
    }

    @Override
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        return null;
    }
}
