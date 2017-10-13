package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.SubTypeMapper;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.housemate.client.api.internal.object.view.NoView;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxySubTypeBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.SubType.Data, SubType.Data, SubType.Listener<? super ProxySubTypeBridge>, NoView>
        implements SubType<ProxyTypeBridge, ProxySubTypeBridge> {

    @Inject
    protected ProxySubTypeBridge(@Assisted Logger logger,
                                 SubTypeMapper subTypeMapper,
                                 ManagedCollectionFactory managedCollectionFactory,
                                 com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                 Sender.Factory v1_0SenderFactory) {
        super(logger, SubType.Data.class, subTypeMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
    }

    @Override
    public ProxyTypeBridge getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        return null;
    }
}
