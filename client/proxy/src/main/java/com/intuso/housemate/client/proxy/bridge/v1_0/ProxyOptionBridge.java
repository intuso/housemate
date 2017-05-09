package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.OptionMapper;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyOptionBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Option.Data, Option.Data, Option.Listener<? super ProxyOptionBridge>>
        implements Option<ProxyListBridge<ProxySubTypeBridge>, ProxyOptionBridge> {

    private final ProxyListBridge<ProxySubTypeBridge> subTypes;

    @Inject
    protected ProxyOptionBridge(@Assisted Logger logger,
                                OptionMapper optionMapper,
                                ManagedCollectionFactory managedCollectionFactory,
                                com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                Sender.Factory v1_0SenderFactory,
                                Factory<ProxyListBridge<ProxySubTypeBridge>> subTypesFactory) {
        super(logger, Option.Data.class, optionMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        subTypes.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Option.SUB_TYPES_ID),
                ChildUtil.name(internalName,
                        Option.SUB_TYPES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public ProxyListBridge<ProxySubTypeBridge> getSubTypes() {
        return subTypes;
    }

    @Override
    public ProxyObjectBridge<?, ?, ?> getChild(String id) {
        if(SUB_TYPES_ID.equals(id))
            return subTypes;
        return null;
    }
}
