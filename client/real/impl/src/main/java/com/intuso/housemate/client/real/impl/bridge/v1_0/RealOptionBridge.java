package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.OptionMapper;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealOptionBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Option.Data, Option.Data, Option.Listener<? super RealOptionBridge>>
        implements Option<RealListBridge<RealSubTypeBridge>, RealOptionBridge> {

    private final RealListBridge<RealSubTypeBridge> subTypes;

    @Inject
    protected RealOptionBridge(@Assisted Logger logger,
                               OptionMapper optionMapper,
                               ManagedCollectionFactory managedCollectionFactory,
                               Receiver.Factory v1_0ReceiverFactory,
                               com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory,
                               RealObjectBridge.Factory<RealListBridge<RealSubTypeBridge>> subTypesFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Option.Data.class, optionMapper, managedCollectionFactory, v1_0ReceiverFactory, internalSenderFactory);
        this.subTypes = subTypesFactory.create(ChildUtil.logger(logger, Option.SUB_TYPES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        subTypes.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Option.SUB_TYPES_ID),
                ChildUtil.name(internalName,
                        Option.SUB_TYPES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        subTypes.uninit();
    }

    @Override
    public RealListBridge<RealSubTypeBridge> getSubTypes() {
        return subTypes;
    }

    @Override
    public RealObjectBridge<?, ?, ?> getChild(String id) {
        if(SUB_TYPES_ID.equals(id))
            return subTypes;
        return null;
    }
}
