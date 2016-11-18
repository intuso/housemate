package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.plugin.api.internal.driver.DeviceDriver;
import com.intuso.housemate.plugin.api.internal.driver.PluginDependency;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base class for all device
 */
public final class RealDeviceImpl
        extends RealObject<Device.Data, Device.Listener<? super RealDeviceImpl>>
        implements RealDevice<RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealPropertyImpl<PluginDependency<DeviceDriver.Factory<?>>>, RealListGeneratedImpl<RealPropertyImpl<?>>,
        RealListGeneratedImpl<RealFeatureImpl>, RealDeviceImpl> {

    private final static String PROPERTIES_DESCRIPTION = "The device's properties";
    private final static String FEATURES_DESCRIPTION = "The device's features";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginDependency<DeviceDriver.Factory<?>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListGeneratedImpl<RealPropertyImpl<?>> properties;
    private final RealListGeneratedImpl<RealFeatureImpl> features;

    private final RemoveCallback<RealDeviceImpl> removeCallback;

    private DeviceDriver driver;

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     */
    @Inject
    public RealDeviceImpl(@Assisted final Logger logger,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted RemoveCallback<RealDeviceImpl> removeCallback,
                          ListenersFactory listenersFactory,
                          AnnotationProcessor annotationProcessor,
                          RealCommandImpl.Factory commandFactory,
                          RealParameterImpl.Factory<String> stringParameterFactory,
                          RealValueImpl.Factory<Boolean> booleanValueFactory,
                          RealValueImpl.Factory<String> stringValueFactory,
                          RealListGeneratedImpl.Factory<RealPropertyImpl<?>> propertiesFactory,
                          RealListGeneratedImpl.Factory<RealFeatureImpl> featuresFactory,
                          RealPropertyImpl.Factory<PluginDependency<DeviceDriver.Factory<? extends DeviceDriver>>> driverPropertyFactory) {
        super(logger, true, new Device.Data(id, name, description), listenersFactory);
        this.annotationProcessor = annotationProcessor;
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
                            if (newName != null && !RealDeviceImpl.this.getName().equals(newName))
                                setName(newName);
                        }
                    }
                },
                Lists.newArrayList(stringParameterFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        Renameable.NAME_ID,
                        Renameable.NAME_ID,
                        "The new name",
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
        this.runningValue = booleanValueFactory.create(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                Runnable.RUNNING_ID,
                Runnable.RUNNING_ID,
                "Whether the device is running or not",
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
        this.errorValue = stringValueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID),
                Failable.ERROR_ID,
                Failable.ERROR_ID,
                "Current error for the device",
                1,
                1,
                Lists.<String>newArrayList());
        this.driverProperty = driverPropertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                UsesDriver.DRIVER_ID,
                UsesDriver.DRIVER_ID,
                "The device's driver",
                1,
                1,
                Lists.<PluginDependency<DeviceDriver.Factory<?>>>newArrayList());
        this.driverLoadedValue = booleanValueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                UsesDriver.DRIVER_LOADED_ID,
                UsesDriver.DRIVER_LOADED_ID,
                "Whether the device's driver is loaded or not",
                1,
                1,
                Lists.newArrayList(false));
        this.properties = propertiesFactory.create(ChildUtil.logger(logger, Device.PROPERTIES_ID),
                Device.PROPERTIES_ID,
                Device.PROPERTIES_ID,
                PROPERTIES_DESCRIPTION,
                Lists.<RealPropertyImpl<?>>newArrayList());
        this.features = featuresFactory.create(ChildUtil.logger(logger, Device.FEATURES_ID),
                Device.FEATURES_ID,
                Device.FEATURES_ID,
                FEATURES_DESCRIPTION,
                Lists.<RealFeatureImpl>newArrayList());
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginDependency<DeviceDriver.Factory<?>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginDependency<DeviceDriver.Factory<?>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginDependency<DeviceDriver.Factory<?>>> factoryRealProperty) {
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
        properties.init(ChildUtil.name(name, Device.PROPERTIES_ID), connection);
        features.init(ChildUtil.name(name, Device.FEATURES_ID), connection);
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
        properties.uninit();
        features.uninit();
    }

    private void setName(String newName) {
        RealDeviceImpl.this.getData().setName(newName);
        for(Device.Listener<? super RealDeviceImpl> listener : listeners)
            listener.renamed(RealDeviceImpl.this, RealDeviceImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginDependency<DeviceDriver.Factory<?>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getDependency().create(logger, this);
                for(RealFeatureImpl feature : annotationProcessor.findFeatures(logger, driver))
                    features.add(feature);
                for(RealPropertyImpl<?> property : annotationProcessor.findProperties(logger, driver))
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

    public <DRIVER extends DeviceDriver> DRIVER getDriver() {
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
    public RealPropertyImpl<PluginDependency<DeviceDriver.Factory<?>>> getDriverProperty() {
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
        removeCallback.removeDevice(this);
    }

    @Override
    public final RealListGeneratedImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    @Override
    public RealListGeneratedImpl<RealFeatureImpl> getFeatures() {
        return features;
    }

    protected final void _start() {
        try {
            if(isDriverLoaded())
                driver.start();
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start device: " + t.getMessage());
        }
    }

    protected final void _stop() {
        if(isDriverLoaded())
            driver.stop();
    }

    @Override
    public void setError(String error) {
        errorValue.setValue(error);
    }

    public interface Factory {
        RealDeviceImpl create(Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 RemoveCallback<RealDeviceImpl> removeCallback);
    }
}
