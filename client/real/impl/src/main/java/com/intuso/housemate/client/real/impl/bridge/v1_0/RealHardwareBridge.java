package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.HardwareMapper;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class RealHardwareBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Hardware.Data, Hardware.Data, Hardware.Listener<? super RealHardwareBridge>>
        implements Hardware<RealCommandBridge,
        RealCommandBridge,
        RealCommandBridge,
        RealValueBridge,
        RealValueBridge,
        RealPropertyBridge,
        RealValueBridge,
        RealListBridge<RealCommandBridge>,
        RealListBridge<RealValueBridge>,
        RealListBridge<RealPropertyBridge>,
        RealHardwareBridge> {

    private final RealCommandBridge renameCommand;
    private final RealCommandBridge removeCommand;
    private final RealValueBridge runningValue;
    private final RealCommandBridge startCommand;
    private final RealCommandBridge stopCommand;
    private final RealValueBridge errorValue;
    private final RealPropertyBridge driverProperty;
    private final RealValueBridge driverLoadedValue;
    private final RealListBridge<RealCommandBridge> commands;
    private final RealListBridge<RealValueBridge> values;
    private final RealListBridge<RealPropertyBridge> properties;

    @Inject
    protected RealHardwareBridge(@Assisted Logger logger,
                                 HardwareMapper hardwareMapper,
                                 RealObjectBridge.Factory<RealCommandBridge> commandFactory,
                                 RealObjectBridge.Factory<RealValueBridge> valueFactory,
                                 RealObjectBridge.Factory<RealPropertyBridge> propertyFactory,
                                 RealObjectBridge.Factory<RealListBridge<RealCommandBridge>> commandsFactory,
                                 RealObjectBridge.Factory<RealListBridge<RealValueBridge>> valuesFactory,
                                 RealObjectBridge.Factory<RealListBridge<RealPropertyBridge>> propertiesFactory,
                                 ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Hardware.Data.class, hardwareMapper, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        runningValue = valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID));
        startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID));
        stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, Hardware.COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, Hardware.VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Hardware.PROPERTIES_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        removeCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID),
                connection);
        runningValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.RUNNING_ID),
                ChildUtil.name(internalName, com.intuso.housemate.client.api.internal.Runnable.RUNNING_ID),
                connection);
        startCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.START_ID),
                ChildUtil.name(internalName, Runnable.START_ID),
                connection);
        stopCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Runnable.STOP_ID),
                ChildUtil.name(internalName, Runnable.STOP_ID),
                connection);
        errorValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID),
                connection);
        driverProperty.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_ID),
                connection);
        driverLoadedValue.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_LOADED_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_LOADED_ID),
                connection);
        commands.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.COMMANDS_ID),
                ChildUtil.name(internalName, Hardware.COMMANDS_ID),
                connection);
        values.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.VALUES_ID),
                ChildUtil.name(internalName, Hardware.VALUES_ID),
                connection);
        properties.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Hardware.PROPERTIES_ID),
                ChildUtil.name(internalName, Hardware.PROPERTIES_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        runningValue.uninit();
        startCommand.uninit();
        stopCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
    }

    @Override
    public RealCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public RealCommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public RealCommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public RealPropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
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
