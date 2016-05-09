package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Base class for all device
 */
public final class RealDeviceImpl<DRIVER extends DeviceDriver>
        extends RealObject<Device.Data, Device.Listener<? super RealDeviceImpl<DRIVER>>>
        implements RealDevice<DRIVER, RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
                RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>>, RealListImpl<RealPropertyImpl<?>>,
                RealListImpl<RealFeatureImpl>, RealDeviceImpl<DRIVER>> {

    private final static String PROPERTIES_DESCRIPTION = "The device's properties";
    private final static String FEATURES_DESCRIPTION = "The device's features";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListImpl<RealPropertyImpl<?>> properties;
    private final RealListImpl<RealFeatureImpl> features;

    private final RemoveCallback<RealDeviceImpl<DRIVER>> removeCallback;

    private DRIVER driver;

    /**
     * @param logger {@inheritDoc}
     * @param data the device's data
     * @param listenersFactory
     */
    @Inject
    public RealDeviceImpl(@Assisted final Logger logger,
                          @Assisted Device.Data data,
                          ListenersFactory listenersFactory,
                          @Assisted RemoveCallback<RealDeviceImpl<DRIVER>> removeCallback,
                          AnnotationProcessor annotationProcessor,
                          DeviceFactoryType driverFactoryType) {
        super(logger, data, listenersFactory);
        this.annotationProcessor = annotationProcessor;
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(ChildUtil.logger(logger, Renameable.RENAME_ID),
                new Command.Data(Renameable.RENAME_ID, Renameable.RENAME_ID, "Rename the device"),
                listenersFactory,
                StringType.createParameter(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        new Parameter.Data(Renameable.NAME_ID, Renameable.NAME_ID, "The new name"),
                        listenersFactory)) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                    String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                    if (newName != null && !RealDeviceImpl.this.getName().equals(newName))
                        setName(newName);
                }
            }
        };
        this.removeCommand = new RealCommandImpl(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                new Command.Data(Removeable.REMOVE_ID, Removeable.REMOVE_ID, "Remove the device"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(isRunning())
                    throw new HousemateException("Cannot remove while device is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                new Value.Data(Runnable.RUNNING_ID, Runnable.RUNNING_ID, "Whether the device is running or not"),
                listenersFactory,
                false);
        this.startCommand = new RealCommandImpl(ChildUtil.logger(logger, Runnable.START_ID),
                new Command.Data(Runnable.START_ID, Runnable.START_ID, "Start the device"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(!isRunning()) {
                    _start();
                    runningValue.setValue(true);
                }
            }
        };
        this.stopCommand = new RealCommandImpl(ChildUtil.logger(logger, Runnable.STOP_ID),
                new Command.Data(Runnable.STOP_ID, Runnable.STOP_ID, "Stop the device"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(isRunning()) {
                    _stop();
                    runningValue.setValue(false);
                }
            }
        };
        this.errorValue = StringType.createValue(ChildUtil.logger(logger, Failable.ERROR_ID),
                new Value.Data(Failable.ERROR_ID, Failable.ERROR_ID, "Current error for the device"),
                listenersFactory,
                null);
        this.driverProperty = (RealPropertyImpl) new RealPropertyImpl(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                new Property.Data(UsesDriver.DRIVER_ID, UsesDriver.DRIVER_ID, "The device's driver"),
                listenersFactory,
                driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                new Value.Data(UsesDriver.DRIVER_LOADED_ID, UsesDriver.DRIVER_LOADED_ID, "Whether the device's driver is loaded or not"),
                listenersFactory,
                false);
        this.properties = new RealListImpl<>(ChildUtil.logger(logger, Device.PROPERTIES_ID), new List.Data(Device.PROPERTIES_ID, Device.PROPERTIES_ID, PROPERTIES_DESCRIPTION), listenersFactory);
        this.features = new RealListImpl<>(ChildUtil.logger(logger, Device.FEATURES_ID), new List.Data(Device.FEATURES_ID, Device.FEATURES_ID, FEATURES_DESCRIPTION), listenersFactory);
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>> factoryRealProperty) {
                initDriver();
            }
        });
        initDriver();
    }

    @Override
    protected void initChildren(String name, Session session) throws JMSException {
        super.initChildren(name, session);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), session);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), session);
        runningValue.init(ChildUtil.name(name, Runnable.RUNNING_ID), session);
        startCommand.init(ChildUtil.name(name, Runnable.START_ID), session);
        stopCommand.init(ChildUtil.name(name, Runnable.STOP_ID), session);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), session);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), session);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), session);
        properties.init(ChildUtil.name(name, Device.PROPERTIES_ID), session);
        features.init(ChildUtil.name(name, Device.FEATURES_ID), session);
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
        for(Device.Listener<? super RealDeviceImpl<DRIVER>> listener : listeners)
            listener.renamed(RealDeviceImpl.this, RealDeviceImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<DeviceDriver.Factory<DRIVER>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(logger, this);
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

    public DRIVER getDriver() {
        return driver;
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
    public RealPropertyImpl<PluginResource<DeviceDriver.Factory<DRIVER>>> getDriverProperty() {
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
    public final RealListImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    @Override
    public RealListImpl<RealFeatureImpl> getFeatures() {
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

    public static interface Factory {
        RealDeviceImpl<?> create(Logger logger, Device.Data data, RemoveCallback<RealDeviceImpl<?>> removeCallback);
    }
}
