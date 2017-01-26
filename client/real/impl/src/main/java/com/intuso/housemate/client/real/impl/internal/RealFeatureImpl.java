package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.bridge.v1_0.driver.FeatureDriverBridge;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.Feature;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.type.TypeRepository;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public final class RealFeatureImpl
        extends RealObject<Feature.Data, Feature.Listener<? super RealFeatureImpl>>
        implements RealFeature<RealCommandImpl,
        RealListGeneratedImpl<RealCommandImpl>,
        RealValueImpl<Boolean>,
        RealValueImpl<String>,
        RealListGeneratedImpl<RealValueImpl<?>>,
        RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>>,
        RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealFeatureImpl> {

    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final AnnotationParser annotationParser;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListGeneratedImpl<RealCommandImpl> commands;
    private final RealListGeneratedImpl<RealValueImpl<?>> values;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;

    private final RealFeature.RemoveCallback<RealFeatureImpl> removeCallback;

    private FeatureDriver driver;

    @Inject
    public RealFeatureImpl(@Assisted final Logger logger,
                           @Assisted("id") String id,
                           @Assisted("name") String name,
                           @Assisted("description") String description,
                           @Assisted RemoveCallback<RealFeatureImpl> removeCallback,
                           ManagedCollectionFactory managedCollectionFactory,
                           AnnotationParser annotationParser,
                           RealCommandImpl.Factory commandFactory,
                           RealParameterImpl.Factory parameterFactory,
                           RealPropertyImpl.Factory propertyFactory,
                           RealValueImpl.Factory valueFactory,
                           RealListGeneratedImpl.Factory<RealCommandImpl> commandsFactory,
                           RealListGeneratedImpl.Factory<RealValueImpl<?>> valuesFactory,
                           RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                           TypeRepository typeRepository) {
        super(logger, new Feature.Data(id, name, description), managedCollectionFactory);
        this.annotationParser = annotationParser;
        this.removeCallback = removeCallback;
        this.renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID),
                Renameable.RENAME_ID,
                Renameable.RENAME_ID,
                "Rename the device",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                            String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                            if (newName != null && !RealFeatureImpl.this.getName().equals(newName))
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
                "Remove the device",
                new RealCommandImpl.Performer() {
                    @Override
                    public void perform(Type.InstanceMap values) {
                        if(isRunning())
                            throw new HousemateException("Cannot remove while device is still running");
                        remove();
                    }
                },
                Lists.<RealParameterImpl<?>>newArrayList());
        this.runningValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                Runnable.RUNNING_ID,
                Runnable.RUNNING_ID,
                "Whether the device is running or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.startCommand = commandFactory.create(ChildUtil.logger(logger, Runnable.START_ID),
                Runnable.START_ID,
                Runnable.START_ID,
                "Start the device",
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
                "Stop the device",
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
                "Current error for the device",
                typeRepository.getType(new TypeSpec(String.class)),
                1,
                1,
                Lists.<String>newArrayList());
        this.driverProperty = (RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>>) propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                UsesDriver.DRIVER_ID,
                UsesDriver.DRIVER_ID,
                "The device's driver",
                typeRepository.getType(new TypeSpec(Types.newParameterizedType(PluginDependency.class, FeatureDriver.class))),
                1,
                1,
                Lists.<PluginDependency<FeatureDriver.Factory<?>>>newArrayList());
        this.driverLoadedValue = (RealValueImpl<Boolean>) valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                UsesDriver.DRIVER_LOADED_ID,
                UsesDriver.DRIVER_LOADED_ID,
                "Whether the device's driver is loaded or not",
                typeRepository.getType(new TypeSpec(Boolean.class)),
                1,
                1,
                Lists.newArrayList(false));
        this.commands = commandsFactory.create(ChildUtil.logger(logger, Feature.COMMANDS_ID),
                Feature.COMMANDS_ID,
                "Commands",
                "The commands of this feature",
                Lists.<RealCommandImpl>newArrayList());
        this.values = valuesFactory.create(ChildUtil.logger(logger, Feature.VALUES_ID),
                Feature.VALUES_ID,
                "Values",
                "The values of this feature",
                Lists.<RealValueImpl<?>>newArrayList());
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, Feature.PROPERTIES_ID),
                Feature.PROPERTIES_ID,
                Feature.PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>> factoryRealProperty) {
                initDriver();
            }
        });
    }

    private void initDriver() {
        if(driver == null) {
            PluginDependency<FeatureDriver.Factory<?>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getDependency().create(logger, this);
                Object annotatedObject;
                if(driver instanceof FeatureDriverBridge)
                    annotatedObject = ((FeatureDriverBridge) driver).getFeatureDriver();
                else
                    annotatedObject = driver;
                for(RealCommandImpl command : annotationParser.findCommands(logger, "", annotatedObject))
                    commands.add(command);
                for(RealValueImpl<?> value : annotationParser.findValues(logger, "", annotatedObject))
                    values.add(value);
                for(RealPropertyImpl<?> property : annotationParser.findProperties(logger, "", annotatedObject))
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
            for (RealPropertyImpl<?> property : Lists.newArrayList(properties))
                properties.remove(property.getId());
        }
    }

    public <DRIVER extends FeatureDriver> DRIVER getDriver() {
        return (DRIVER) driver;
    }

    private void setName(String newName) {
        getData().setName(newName);
        for(Feature.Listener<? super RealFeatureImpl> listener : listeners)
            listener.renamed(RealFeatureImpl.this, RealFeatureImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), connection);
        commands.init(ChildUtil.name(name, Feature.COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, Feature.VALUES_ID), connection);
        properties.init(ChildUtil.name(name, Feature.PROPERTIES_ID), connection);
        // at the end as init'ing it might init the driver and set values, add commands/properties etc
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
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
        removeCallback.removeFeature(this);
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    @Override
    public RealPropertyImpl<PluginDependency<FeatureDriver.Factory<?>>> getDriverProperty() {
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
    public final RealListGeneratedImpl<RealCommandImpl> getCommands() {
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
                driver.init(logger, this);
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start device: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.uninit();
    }

    public interface Factory {
        RealFeatureImpl create(Logger logger,
                               @Assisted("id") String id,
                               @Assisted("name") String name,
                               @Assisted("description") String description,
                               RemoveCallback<RealFeatureImpl> removeCallback);
    }
}
