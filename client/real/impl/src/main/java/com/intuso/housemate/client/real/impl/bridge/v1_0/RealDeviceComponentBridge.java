package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceComponentMapper;
import com.intuso.housemate.client.api.internal.object.DeviceComponent;
import com.intuso.housemate.client.api.internal.object.view.DeviceComponentView;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Set;

/**
 * Created by tomc on 28/11/16.
 */
public class RealDeviceComponentBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.DeviceComponent.Data, DeviceComponent.Data, DeviceComponent.Listener<? super RealDeviceComponentBridge>, DeviceComponentView>
        implements DeviceComponent<
            RealListBridge<RealCommandBridge>,
            RealListBridge<RealValueBridge>,
            RealDeviceComponentBridge> {

    private final RealListBridge<RealCommandBridge> commands;
    private final RealListBridge<RealValueBridge> values;

    @Inject
    protected RealDeviceComponentBridge(@Assisted Logger logger,
                                        DeviceComponentMapper deviceComponentMapper,
                                        ManagedCollectionFactory managedCollectionFactory,
                                        Factory<RealListBridge<RealCommandBridge>> commandsFactory,
                                        Factory<RealListBridge<RealValueBridge>> valuesFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.DeviceComponent.Data.class, deviceComponentMapper, managedCollectionFactory);
        commands = commandsFactory.create(ChildUtil.logger(logger, DeviceComponent.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, DeviceComponent.VALUES_ID));
    }

    @Override
    public Set<String> getClasses() {
        return getData().getClasses();
    }

    @Override
    public Set<String> getAbilities() {
        return data.getAbilities();
    }

    @Override
    protected void initChildren(String versionName, String internalName, Sender.Factory internalSenderFactory, com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, Receiver.Factory v1_0ReceiverFactory) {
        super.initChildren(versionName, internalName, internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        commands.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.DeviceComponent.COMMANDS_ID),
                ChildUtil.name(internalName, DeviceComponent.COMMANDS_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        values.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.DeviceComponent.VALUES_ID),
                ChildUtil.name(internalName, DeviceComponent.VALUES_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        commands.uninit();
        values.uninit();
    }

    @Override
    public RealListBridge<RealCommandBridge> getCommands() {
        return commands;
    }

    @Override
    public RealListBridge<RealValueBridge> getValues() {
        return values;
    }

    @Override
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }
}
