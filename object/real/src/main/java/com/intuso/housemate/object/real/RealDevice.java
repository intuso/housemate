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

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealDevice
        extends RealPrimaryObject<DeviceWrappable, RealDevice, DeviceListener<? super RealDevice>>
        implements Device<RealCommand, RealCommand, RealCommand, RealList<CommandWrappable, RealCommand>,
            RealValue<Boolean>, RealValue<Boolean>, RealValue<String>, RealValue<?>, RealList<ValueWrappable, RealValue<?>>,
            RealProperty<String>, RealProperty<?>, RealList<PropertyWrappable, RealProperty<?>>, RealDevice> {

    private final static String COMMANDS_DESCRIPTION = "The device's commands";
    private final static String VALUES_DESCRIPTION = "The device's values";
    private final static String PROPERTIES_DESCRIPTION = "The device's properties";

    public final static String OBJECT_TYPE = "device";

    private RealList<CommandWrappable, RealCommand> commands;
    private RealList<ValueWrappable, RealValue<?>> values;
    private RealList<PropertyWrappable, RealProperty<?>> properties;

    public RealDevice(RealResources resources, String id, String name, String description) {
        this(resources, id, name, description, new ArrayList<RealCommand>(0), new ArrayList<RealValue<?>>(0), new ArrayList<RealProperty<?>>(0));
    }

    public RealDevice(RealResources resources, String id, String name, String description, List<RealCommand> commands, List<RealValue<?>> values, List<RealProperty<?>> properties) {
        super(resources, new DeviceWrappable(id, name, description), OBJECT_TYPE);
        this.commands = new RealList<CommandWrappable, RealCommand>(resources, COMMANDS, COMMANDS, COMMANDS_DESCRIPTION, commands);
        this.values = new RealList<ValueWrappable, RealValue<?>>(resources, VALUES, VALUES, VALUES_DESCRIPTION, values);
        this.properties = new RealList<PropertyWrappable, RealProperty<?>>(resources, PROPERTIES, PROPERTIES, PROPERTIES_DESCRIPTION, properties);
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

    protected final void remove() {
        getRealRoot().removeDevice(getId());
    }

    protected final void _start() {
        if(isRunning())
            return;
        try {
            start();
            getRunningValue().setTypedValue(Boolean.TRUE);
        } catch (HousemateException e) {
            getErrorValue().setTypedValue("Could not start device: " + e.getMessage());
        }
    }

    protected final void _stop() {
        if(!isRunning())
            return;
        stop();
        getRunningValue().setTypedValue(Boolean.FALSE);
    }

    protected void start() throws HousemateException {};
    protected void stop() {};
}
