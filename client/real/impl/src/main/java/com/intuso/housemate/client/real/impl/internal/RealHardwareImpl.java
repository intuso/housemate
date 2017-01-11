package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.Hardware;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base class for all hardwares
 */
public final class RealHardwareImpl
        extends RealObject<Hardware.Data, Hardware.Listener<? super RealHardwareImpl>>
        implements RealHardware<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>>, RealListGeneratedImpl<RealCommandImpl>,
        RealListGeneratedImpl<RealValueImpl<?>>, RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealHardwareImpl> {

    private final static String COMMANDS_DESCRIPTION = "The hardware's commands";
    private final static String VALUES_DESCRIPTION = "The hardware's values";
    private final static String PROPERTIES_DESCRIPTION = "The hardware's properties";

    private final AnnotationParser annotationParser;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;

    private final RemoveCallback<RealHardwareImpl> removeCallback;

    private HardwareDriver driver;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealHardwareImpl(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted RemoveCallback<RealHardwareImpl> removeCallback,
                            ListenersFactory listenersFactory,
                            AnnotationParser annotationParser,
                            RealCommandImpl.Factory commandFactory,
                            RealParameterImpl.Factory parameterFactory,
                            RealPropertyImpl.Factory propertyFactory,
                            RealValueImpl.Factory valueFactory,
                            TypeRepository typeRepository,
                            RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                            RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                            RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory) {
        super(logger, true, new Hardware.Data(id, name, description), listenersFactory);
        this.annotationParser = annotationParser;
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if (values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealHardwareImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(parameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
                        typeRepository.getType(new TypeSpec(String.class)),
                        1,
                        1)));
        this.removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                Removeable.REMOVE_ID,
                Removeable.REMOVE_ID,
                "Remove the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning())
                            throw new HousemateException("Cannot remove while hardware is still running");
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.runningValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                Runnable.RUNNING_ID,
                Runnable.RUNNING_ID,
                "Whether the hardware is running or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID),
                Runnable.START_ID,
                Runnable.START_ID,
                "Start the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(!isRunning()) {
                            _start();
                            runningValue.setValue(true);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.stopCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.STOP_ID),
                Runnable.STOP_ID,
                Runnable.STOP_ID,
                "Stop the hardware",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning()) {
                            _stop();
                            runningValue.setValue(false);
                        }
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.errorValue = (RealValueImpl<String>) valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the hardware",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.driverProperty = (RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>>) propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                UsesDriver.DRIVER_ID,
                UsesDriver.DRIVER_ID,
                "The hardware's driver",
                typeRepository.getType(new TypeSpec(Types.newParameterizedType(PluginDependency.class, HardwareDriver.class))),
                1,
                1,
                Lists.<PluginDependency<HardwareDriver.Factory<?>>>newArrayList());
        this.driverLoadedValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                UsesDriver.DRIVER_LOADED_ID,
                UsesDriver.DRIVER_LOADED_ID,
                "Whether the hardware's driver is loaded or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.commands = commandsFactory.create(ChildUtil.logger(logger, Hardware.COMMANDS_ID),
                Hardware.COMMANDS_ID,
                Hardware.COMMANDS_ID,
                COMMANDS_DESCRIPTION,
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, Hardware.VALUES_ID),
                Hardware.VALUES_ID,
                Hardware.VALUES_ID,
                VALUES_DESCRIPTION,
                Lists.<RealValueImpl<?>>newArrayList());
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, Hardware.PROPERTIES_ID),
                Hardware.PROPERTIES_ID,
                Hardware.PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID), connection);
        startCommand.init(ChildUtil.name(name, Runnable.START_ID), connection);
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID), connection);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), connection);
        commands.init(ChildUtil.name(name, Hardware.COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, Hardware.VALUES_ID), connection);
        properties.init(ChildUtil.name(name, Hardware.PROPERTIES_ID), connection);
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

    private void setName(String newName) {
        RealHardwareImpl.this.getData().setName(newName);
        for(Hardware.Listener<? super RealHardwareImpl> listener : listeners)
            listener.renamed(RealHardwareImpl.this, RealHardwareImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginDependency<HardwareDriver.Factory<?>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getDependency().create(logger, this);
                for(RealCommandImpl command : annotationParser.findCommands(logger, "", driver))
                    commands.add(command);
                for(RealValueImpl<?> value : annotationParser.findValues(logger, "", driver))
                    values.add(value);
                for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, "", driver))
                    properties.add(property);
                errorValue.setValue((String) null);
                driverLoadedValue.setValue(true);
                _start();
            }
        }
    }

    private void uninitDriver() {
        if(driver != null) {
            _stop();
            driverLoadedValue.setValue(false);
            errorValue.setValue("Driver not loaded");
            driver = null;
            for (RealCommandImpl command : Lists.newArrayList(commands))
                commands.remove(command.getId());
            for (RealValueImpl<?> value : Lists.newArrayList(values))
                values.remove(value.getId());
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    public <DRIVER extends HardwareDriver> DRIVER getDriver() {
        return (DRIVER) driver;
    }

    @Override
    public RealCommandImpl getRenameCommand() {
        return renameCommand;
    }

    @Override
    public RealCommandImpl getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public RealValueImpl<String> getErrorValue() {
        return errorValue;
    }

    @Override
    public RealPropertyImpl<PluginDependency<HardwareDriver.Factory<?>>> getDriverProperty() {
        return driverProperty;
    }

    @Override
    public RealValueImpl<Boolean> getDriverLoadedValue() {
        return driverLoadedValue;
    }

    public boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null ? driverLoadedValue.getValue() : false;
    }

    @Override
    public RealCommandImpl getStopCommand() {
        return stopCommand;
    }

    @Override
    public RealCommandImpl getStartCommand() {
        return startCommand;
    }

    @Override
    public RealValueImpl<Boolean> getRunningValue() {
        return runningValue;
    }

    public boolean isRunning() {
        return runningValue.getValue() != null ? runningValue.getValue() : false;
    }

    protected final void remove() {
        removeCallback.removeHardware(this);
    }

    @Override
    public RealListGeneratedImpl<RealCommandImpl> getCommands() {
        return commands;
    }

    @Override
    public RealListGeneratedImpl<RealValueImpl<?>> getValues() {
        return values;
    }

    @Override
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    protected final void _start() {
        try {
            if(isDriverLoaded())
                driver.startHardware();
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start hardware: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.stopHardware();
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    @Override
    public void addObject(Object object, String prefix) {
        for(RealCommandImpl command : annotationParser.findCommands(logger, "", object))
            commands.add(command);
        for(RealValueImpl<?> value : annotationParser.findValues(logger, "", object))
            values.add(value);
        for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, "", object))
            properties.add(property);
    }

    public interface Factory {
        RealHardwareImpl create(Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description,
                                RemoveCallback<RealHardwareImpl> removeCallback);
    }
}
