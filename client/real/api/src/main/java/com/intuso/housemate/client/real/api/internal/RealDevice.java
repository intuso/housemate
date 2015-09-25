package com.intuso.housemate.client.real.api.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.impl.type.BooleanType;
import com.intuso.housemate.client.real.api.internal.impl.type.StringType;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Device;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Base class for all devices
 */
public class RealDevice
        extends RealObject<
            DeviceData,
            HousemateData<?>,
            RealObject<?, ?, ?, ?>,
            Device.Listener<? super RealDevice>>
        implements Device<
            RealCommand,
            RealCommand,
            RealCommand,
            RealCommand,
            RealList<CommandData, RealCommand>,
            RealValue<Boolean>,
            RealValue<String>,
            RealValue<?>,
            RealList<ValueData, RealValue<?>>,
            RealProperty<?>,
            RealList<PropertyData, RealProperty<?>>,
            RealDevice> {

    private final static String COMMANDS_DESCRIPTION = "The device's commands";
    private final static String VALUES_DESCRIPTION = "The device's values";
    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    private final String type;

    private final RealCommand rename;
    private final RealCommand remove;
    private final RealValue<Boolean> running;
    private final RealCommand start;
    private final RealCommand stop;
    private final RealValue<String> error;
    private final RealList<CommandData, RealCommand> commands;
    private final RealList<ValueData, RealValue<?>> values;
    private final RealList<PropertyData, RealProperty<?>> properties;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the device's data
     * @param featureIds the ids of the features the device has
     */
    public RealDevice(Log log, ListenersFactory listenersFactory, String type, DeviceData data, String... featureIds) {
        this(log, listenersFactory, type, data, Lists.newArrayList(featureIds),
                Lists.<String>newArrayList(), Lists.<String>newArrayList(), Lists.<String>newArrayList());
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data the device's data
     * @param featureIds the ids of the features the device has
     * @param customCommandIds the ids of the device's commands that do not belong to a feature
     * @param customValueIds the ids of the device's values that do not belong to a feature
     * @param customPropertyIds the ids of the device's properties that do not belong to a feature
     */
    public RealDevice(Log log, ListenersFactory listenersFactory, String type, DeviceData data, List<String> featureIds,
                      List<String> customCommandIds, List<String> customValueIds, List<String> customPropertyIds) {
        super(log, listenersFactory,
                new DeviceData(data.getId(), data.getName(), data.getDescription(), featureIds, customCommandIds, customValueIds, customPropertyIds));
        this.rename = new RealCommand(log, listenersFactory, DeviceData.RENAME_ID, DeviceData.RENAME_ID, "Rename the device", Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, DeviceData.NAME_ID, DeviceData.NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(values != null && values.getChildren().containsKey(DeviceData.NAME_ID)) {
                    String newName = values.getChildren().get(DeviceData.NAME_ID).getFirstValue();
                    if (newName != null && !RealDevice.this.getName().equals(newName)) {
                        RealDevice.this.getData().setName(newName);
                        for(Device.Listener<? super RealDevice> listener : RealDevice.this.getObjectListeners())
                            listener.renamed(RealDevice.this, RealDevice.this.getName(), newName);
                        RealDevice.this.sendMessage(DeviceData.NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.remove = new RealCommand(log, listenersFactory, DeviceData.REMOVE_ID, DeviceData.REMOVE_ID, "Remove the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning())
                    throw new HousemateCommsException("Cannot remove while device is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(log, listenersFactory, DeviceData.RUNNING_ID, DeviceData.RUNNING_ID, "Whether the device" + " is running or not", false);
        this.start = new RealCommand(log, listenersFactory, DeviceData.START_ID, DeviceData.START_ID, "Start the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(!isRunning()) {
                    _start();
                    running.setTypedValues(true);
                }
            }
        };
        this.stop = new RealCommand(log, listenersFactory, DeviceData.STOP_ID, DeviceData.STOP_ID, "Stop the device", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                if(isRunning()) {
                    _stop();
                    running.setTypedValues(false);
                }
            }
        };
        this.error = StringType.createValue(log, listenersFactory, DeviceData.ERROR_ID, DeviceData.ERROR_ID, "Current error for the device", null);
        this.type = type;
        this.commands = new RealList<>(log, listenersFactory, DeviceData.COMMANDS_ID, DeviceData.COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = new RealList<>(log, listenersFactory, DeviceData.VALUES_ID, DeviceData.VALUES_ID, VALUES_DESCRIPTION);
        this.properties = new RealList<>(log, listenersFactory, DeviceData.PROPERTIES_ID, DeviceData.PROPERTIES_ID, PROPERTIES_DESCRIPTION);
        addChild(this.rename);
        addChild(this.remove);
        addChild(this.running);
        addChild(this.start);
        addChild(this.stop);
        addChild(this.error);
        addChild(this.commands);
        addChild(this.values);
        addChild(this.properties);
    }

    @Override
    public RealCommand getRenameCommand() {
        return rename;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return error;
    }

    @Override
    public RealCommand getStopCommand() {
        return stop;
    }

    @Override
    public RealCommand getStartCommand() {
        return start;
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return running;
    }

    public boolean isRunning() {
        return running.getTypedValue() != null ? running.getTypedValue() : false;
    }

    public String getType() {
        return type;
    }

    @Override
    public final RealList<CommandData, RealCommand> getCommands() {
        return commands;
    }

    @Override
    public final RealList<ValueData, RealValue<?>> getValues() {
        return values;
    }

    @Override
    public final RealList<PropertyData, RealProperty<?>> getProperties() {
        return properties;
    }

    protected final void remove() {
        getRealRoot().removeDevice(this);
    }

    protected final void _start() {
        try {
            start();
        } catch (Throwable t) {
            getErrorValue().setTypedValues("Could not start device: " + t.getMessage());
        }
    }

    protected final void _stop() {
        stop();
    }

    @Override
    public final List<String> getFeatureIds() {
        return getData().getFeatureIds();
    }

    @Override
    public final List<String> getCustomCommandIds() {
        return getData().getCustomCommandIds();
    }

    @Override
    public final List<String> getCustomValueIds() {
        return getData().getCustomValueIds();
    }

    @Override
    public final List<String> getCustomPropertyIds() {
        return getData().getCustomPropertyIds();
    }

    /**
     * Starts the actual implementation of the device
     */
    protected void start() {};

    /**
     * Stops the actual implementation of the device
     */
    protected void stop() {};
}
