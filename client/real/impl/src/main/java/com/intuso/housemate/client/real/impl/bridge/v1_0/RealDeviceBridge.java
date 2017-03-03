package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceMapper;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealDeviceBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Device.Data, Device.Data, Device.Listener<? super RealDeviceBridge>>
        implements Device<RealCommandBridge,
                RealListBridge<RealCommandBridge>,
                RealListBridge<RealValueBridge>,
                RealListBridge<RealPropertyBridge>,
        RealDeviceBridge> {

    private final RealCommandBridge renameCommand;
    private final RealListBridge<RealCommandBridge> commands;
    private final RealListBridge<RealValueBridge> values;
    private final RealListBridge<RealPropertyBridge> properties;

    @Inject
    protected RealDeviceBridge(@Assisted Logger logger,
                               DeviceMapper hardwareMapper,
                               ManagedCollectionFactory managedCollectionFactory,
                               Receiver.Factory v1_0ReceiverFactory,
                               com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory,
                               Factory<RealCommandBridge> commandFactory,
                               Factory<RealListBridge<RealCommandBridge>> commandsFactory,
                               Factory<RealListBridge<RealValueBridge>> valuesFactory,
                               Factory<RealListBridge<RealPropertyBridge>> propertiesFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Device.Data.class, hardwareMapper, managedCollectionFactory, v1_0ReceiverFactory, internalSenderFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, Device.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Device.VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Device.PROPERTIES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID)
        );
        commands.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.COMMANDS_ID),
                ChildUtil.name(internalName, Device.COMMANDS_ID)
        );
        values.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.VALUES_ID),
                ChildUtil.name(internalName, Device.VALUES_ID)
        );
        properties.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Device.PROPERTIES_ID),
                ChildUtil.name(internalName, Device.PROPERTIES_ID)
        );
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
    }

    @Override
    public RealCommandBridge getRenameCommand() {
        return renameCommand;
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
    public RealListBridge<RealPropertyBridge> getProperties() {
        return properties;
    }
}
