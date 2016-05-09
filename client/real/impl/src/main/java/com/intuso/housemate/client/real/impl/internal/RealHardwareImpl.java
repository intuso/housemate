package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.client.real.impl.internal.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * Base class for all hardwares
 */
public final class RealHardwareImpl<DRIVER extends HardwareDriver>
        extends RealObject<Hardware.Data, Hardware.Listener<? super RealHardwareImpl<DRIVER>>>
        implements RealHardware<DRIVER, RealCommandImpl, RealValueImpl<Boolean>, RealValueImpl<String>,
        RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>>, RealListImpl<RealPropertyImpl<?>>,
        RealHardwareImpl<DRIVER>> {

    private final static String PROPERTIES_DESCRIPTION = "The hardware's properties";

    private final AnnotationProcessor annotationProcessor;

    private final RealCommandImpl renameCommand;
    private final RealCommandImpl removeCommand;
    private final RealValueImpl<Boolean> runningValue;
    private final RealCommandImpl startCommand;
    private final RealCommandImpl stopCommand;
    private final RealValueImpl<String> errorValue;
    private final RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>> driverProperty;
    private final RealValueImpl<Boolean> driverLoadedValue;
    private final RealListImpl<RealPropertyImpl<?>> properties;

    private final RemoveCallback<RealHardwareImpl<DRIVER>> removeCallback;

    private DRIVER driver;

    /**
     * @param logger {@inheritDoc}
     * @param data the hardware's data
     * @param listenersFactory
     */
    @Inject
    public RealHardwareImpl(@Assisted Logger logger,
                            @Assisted Hardware.Data data,
                            ListenersFactory listenersFactory,
                            @Assisted RemoveCallback<RealHardwareImpl<DRIVER>> removeCallback,
                            AnnotationProcessor annotationProcessor,
                            HardwareFactoryType driverFactoryType) {
        super(logger, data, listenersFactory);
        this.annotationProcessor = annotationProcessor;
        this.removeCallback = removeCallback;
        this.renameCommand = new RealCommandImpl(ChildUtil.logger(logger, Renameable.RENAME_ID),
                new Command.Data(Renameable.RENAME_ID, Renameable.RENAME_ID, "Rename the hardware"),
                listenersFactory,
                StringType.createParameter(ChildUtil.logger(logger, Renameable.RENAME_ID, Renameable.NAME_ID),
                        new Parameter.Data(Renameable.NAME_ID, Renameable.NAME_ID, "The new name"),
                        listenersFactory)) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(values != null && values.getChildren().containsKey(Renameable.NAME_ID)) {
                    String newName = values.getChildren().get(Renameable.NAME_ID).getFirstValue();
                    if (newName != null && !RealHardwareImpl.this.getName().equals(newName))
                        setName(newName);
                }
            }
        };
        this.removeCommand = new RealCommandImpl(ChildUtil.logger(logger, Removeable.REMOVE_ID),
                new Command.Data(Removeable.REMOVE_ID, Removeable.REMOVE_ID, "Remove the hardware"),
                listenersFactory) {
            @Override
            public void perform(Type.InstanceMap values) {
                if(isRunning())
                    throw new HousemateException("Cannot remove while hardware is still running");
                remove();
            }
        };
        this.runningValue = BooleanType.createValue(ChildUtil.logger(logger, Runnable.RUNNING_ID),
                new Value.Data(Runnable.RUNNING_ID, Runnable.RUNNING_ID, "Whether the hardware is running or not"),
                listenersFactory,
                false);
        this.startCommand = new RealCommandImpl(ChildUtil.logger(logger, Runnable.START_ID),
                new Command.Data(Runnable.START_ID, Runnable.START_ID, "Start the hardware"),
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
                new Command.Data(Runnable.STOP_ID, Runnable.STOP_ID, "Stop the hardware"),
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
                new Value.Data(Failable.ERROR_ID, Failable.ERROR_ID, "Current error for the hardware"),
                listenersFactory,
                null);
        this.driverProperty = (RealPropertyImpl) new RealPropertyImpl(ChildUtil.logger(logger, UsesDriver.DRIVER_ID),
                new Property.Data(UsesDriver.DRIVER_ID, UsesDriver.DRIVER_ID, "The hardware's driver"),
                listenersFactory,
                driverFactoryType);
        this.driverLoadedValue = BooleanType.createValue(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID),
                new Value.Data(UsesDriver.DRIVER_LOADED_ID, UsesDriver.DRIVER_LOADED_ID, "Whether the hardware's driver is loaded or not"),
                listenersFactory,
                false);
        this.properties = new RealListImpl<>(ChildUtil.logger(logger, Hardware.PROPERTIES_ID), new List.Data(Hardware.PROPERTIES_ID, Hardware.PROPERTIES_ID, PROPERTIES_DESCRIPTION), listenersFactory);
        driverProperty.addObjectListener(new Property.Listener<RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>>>() {
            @Override
            public void valueChanging(RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>> factoryRealProperty) {
                uninitDriver();
            }

            @Override
            public void valueChanged(RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>> factoryRealProperty) {
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
        properties.init(ChildUtil.name(name, Hardware.PROPERTIES_ID), session);
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
    }

    private void setName(String newName) {
        RealHardwareImpl.this.getData().setName(newName);
        for(Hardware.Listener<? super RealHardwareImpl<DRIVER>> listener : listeners)
            listener.renamed(RealHardwareImpl.this, RealHardwareImpl.this.getName(), newName);
        data.setName(newName);
        sendData();
    }

    private void initDriver() {
        if(driver == null) {
            PluginResource<HardwareDriver.Factory<DRIVER>> driverFactory = driverProperty.getValue();
            if(driverFactory != null) {
                driver = driverFactory.getResource().create(logger, this);
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
    public RealPropertyImpl<PluginResource<HardwareDriver.Factory<DRIVER>>> getDriverProperty() {
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
    public final RealListImpl<RealPropertyImpl<?>> getProperties() {
        return properties;
    }

    protected final void _start() {
        try {
            if(isDriverLoaded())
                driver.start();
        } catch (Throwable t) {
            getErrorValue().setValue("Could not start hardware: " + t.getMessage());
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
        RealHardwareImpl<?> create(Logger logger, Hardware.Data data, RemoveCallback<RealHardwareImpl<?>> removeCallback);
    }
}
