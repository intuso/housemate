package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;

import java.util.List;

/**
 * Base class for all devices
 */
public class RealDevice
        extends RealPrimaryObject<
        DeviceData,
            RealDevice,
            DeviceListener<? super RealDevice>>
        implements Device<
            RealCommand,
            RealCommand,
            RealCommand,
            RealList<CommandData, RealCommand>,
            RealValue<Boolean>,
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

    public final static String OBJECT_TYPE = "device";

    private RealList<CommandData, RealCommand> commands;
    private RealList<ValueData, RealValue<?>> values;
    private RealList<PropertyData, RealProperty<?>> properties;

    /**
     * @param resources {@inheritDoc}
     * @param id the device's id
     * @param name the device's name
     * @param description the device's description
     */
    public RealDevice(RealResources resources, String id, String name, String description, String ... featureIds) {
        this(resources, id, name, description, Lists.newArrayList(featureIds), Lists.<String>newArrayList(),
                Lists.<String>newArrayList(), Lists.<String>newArrayList());
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the device's id
     * @param name the device's name
     * @param description the device's description
     * @param customCommandIds the ids of the device's commands that do not belong to a feature
     * @param customValueIds the ids of the device's values that do not belong to a feature
     * @param customPropertyIds the ids of the device's properties that do not belong to a feature
     */
    public RealDevice(RealResources resources, String id, String name, String description, List<String> featureIds,
                      List<String> customCommandIds, List<String> customValueIds, List<String> customPropertyIds) {
        super(resources,
                new DeviceData(id, name, description, featureIds, customCommandIds, customValueIds, customPropertyIds),
                OBJECT_TYPE);
        this.commands = new RealList<CommandData, RealCommand>(resources, COMMANDS_ID, COMMANDS_ID, COMMANDS_DESCRIPTION);
        this.values = new RealList<ValueData, RealValue<?>>(resources, VALUES_ID, VALUES_ID, VALUES_DESCRIPTION);
        this.properties = new RealList<PropertyData, RealProperty<?>>(resources, PROPERTIES_ID, PROPERTIES_ID, PROPERTIES_DESCRIPTION);
        addChild(this.commands);
        addChild(this.values);
        addChild(this.properties);
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

    @Override
    public boolean isConnected() {
        throw new HousemateRuntimeException("This value is not maintained by the client");
    }

    @Override
    public RealValue<Boolean> getConnectedValue() {
        throw new HousemateRuntimeException("This value is not maintained by the client");
    }

    @Override
    protected final void remove() {
        getRealRoot().removeDevice(getId());
    }

    @Override
    protected final void _start() {
        try {
            start();
        } catch (HousemateException e) {
            getErrorValue().setTypedValues("Could not start device: " + e.getMessage());
        }
    }

    @Override
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
     * @throws HousemateException if the implementation fails to start
     */
    protected void start() throws HousemateException {};

    /**
     * Stops the actual implementation of the device
     */
    protected void stop() {};
}
