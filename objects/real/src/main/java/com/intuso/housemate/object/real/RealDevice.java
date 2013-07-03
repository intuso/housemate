package com.intuso.housemate.object.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

import java.util.ArrayList;
import java.util.List;

public class RealDevice
        extends RealPrimaryObject<
            DeviceWrappable,
            RealDevice,
            DeviceListener<? super RealDevice>>
        implements Device<
            RealCommand,
            RealCommand,
            RealCommand,
            RealList<CommandWrappable, RealCommand>,
            RealValue<Boolean>,
            RealValue<Boolean>,
            RealValue<String>,
            RealValue<?>,
            RealList<ValueWrappable, RealValue<?>>,
            RealProperty<?>,
            RealList<PropertyWrappable, RealProperty<?>>,
            RealDevice> {

    private final static String COMMANDS_DESCRIPTION = "The device's commands";
    private final static String VALUES_DESCRIPTION = "The device's values";
    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    public final static String OBJECT_TYPE = "device";

    private RealList<CommandWrappable, RealCommand> commands;
    private RealList<ValueWrappable, RealValue<?>> values;
    private RealList<PropertyWrappable, RealProperty<?>> properties;

    /**
     * @param resources {@inheritDoc}
     * @param id the device's id
     * @param name the device's name
     * @param description the device's description
     */
    public RealDevice(RealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<RealCommand>(0), new ArrayList<RealValue<?>>(0), new ArrayList<RealProperty<?>>(0));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the device's id
     * @param name the device's name
     * @param description the device's description
     * @param commands the device's commands
     * @param values the device's values
     * @param properties the device's properties
     */
    public RealDevice(RealResources resources, String id, String name, String description, List<RealCommand> commands, List<RealValue<?>> values, List<RealProperty<?>> properties) {
        super(resources, new DeviceWrappable(id, name, description), OBJECT_TYPE);
        this.commands = new RealList<CommandWrappable, RealCommand>(resources, COMMANDS_ID, COMMANDS_ID, COMMANDS_DESCRIPTION, commands);
        this.values = new RealList<ValueWrappable, RealValue<?>>(resources, VALUES_ID, VALUES_ID, VALUES_DESCRIPTION, values);
        this.properties = new RealList<PropertyWrappable, RealProperty<?>>(resources, PROPERTIES_ID, PROPERTIES_ID, PROPERTIES_DESCRIPTION, properties);
        addWrapper(this.commands);
        addWrapper(this.values);
        addWrapper(this.properties);
    }

    @Override
    public final RealList<CommandWrappable, RealCommand> getCommands() {
        return commands;
    }

    @Override
    public final RealList<ValueWrappable, RealValue<?>> getValues() {
        return values;
    }

    @Override
    public final RealList<PropertyWrappable, RealProperty<?>> getProperties() {
        return properties;
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
