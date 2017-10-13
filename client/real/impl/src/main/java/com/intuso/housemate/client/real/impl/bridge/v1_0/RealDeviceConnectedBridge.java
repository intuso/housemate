package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.DeviceConnectedMapper;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealDeviceConnectedBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Device.Connected.Data, Device.Connected.Data, Device.Connected.Listener<? super RealDeviceConnectedBridge>, DeviceConnectedView>
        implements Device.Connected<RealCommandBridge,
                RealListBridge<RealCommandBridge>,
                RealListBridge<RealValueBridge>,
        RealDeviceConnectedBridge> {

    private final RealCommandBridge renameCommand;
    private final RealListBridge<RealCommandBridge> commands;
    private final RealListBridge<RealValueBridge> values;

    @Inject
    protected RealDeviceConnectedBridge(@Assisted Logger logger,
                                        DeviceConnectedMapper deviceConnectedMapper,
                                        ManagedCollectionFactory managedCollectionFactory,
                                        Receiver.Factory v1_0ReceiverFactory,
                                        com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory,
                                        Factory<RealCommandBridge> commandFactory,
                                        Factory<RealListBridge<RealCommandBridge>> commandsFactory,
                                        Factory<RealListBridge<RealValueBridge>> valuesFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Device.Connected.Data.class, deviceConnectedMapper, managedCollectionFactory, v1_0ReceiverFactory, internalSenderFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, Device.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Device.VALUES_ID));
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
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
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
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }
}
