package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ReferenceMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Reference;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyReferenceBridge<OBJECT_VIEW extends View, OBJECT extends ProxyObjectBridge<?, ?, ?, OBJECT_VIEW>>
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Reference.Data, Reference.Data, Reference.Listener<? super ProxyReferenceBridge<OBJECT_VIEW, OBJECT>>, OBJECT_VIEW>
        implements Reference<OBJECT_VIEW, OBJECT, ProxyReferenceBridge<OBJECT_VIEW, OBJECT>> {

    @Inject
    protected ProxyReferenceBridge(@Assisted Logger logger,
                                   ReferenceMapper referenceMapper,
                                   ManagedCollectionFactory managedCollectionFactory,
                                   com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                   Sender.Factory v1_0SenderFactory) {
        super(logger, Reference.Data.class, referenceMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
    }

    @Override
    public OBJECT get() {
        throw new UnsupportedOperationException("This bridge is just for converting messages between api versions. References should be loaded from a real or proxy version of this object");
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        return null;
    }
}
