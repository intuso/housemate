package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child's resources
 * @param <COMMAND> the type of the commands
 * @param <COMMANDS> the type of the commands list
 * @param <VALUE> the type of the values
 * @param <VALUES> the type of the values list
 * @param <PROPERTY> the type of the properties
 * @param <PROPERTIES> the type of the properties list
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDevice<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            COMMAND extends ProxyCommand<?, ?, ?, ?, COMMAND>,
            COMMANDS extends ProxyList<?, ?, CommandWrappable, COMMAND, COMMANDS>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            VALUES extends ProxyList<?, ?, ValueWrappable, VALUE, VALUES>,
            PROPERTY extends ProxyProperty<?, ?, ?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<?, ?, PropertyWrappable, PROPERTY, PROPERTIES>,
            DEVICE extends ProxyDevice<RESOURCES, CHILD_RESOURCES, COMMAND, COMMANDS, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE>>
        extends ProxyPrimaryObject<RESOURCES, CHILD_RESOURCES, DeviceWrappable, COMMAND, VALUE, DEVICE, DeviceListener<? super DEVICE>>
        implements Device<COMMAND, COMMAND, COMMAND, COMMANDS, VALUE, VALUE, VALUE, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE> {

    private COMMANDS commandList;
    private VALUES valueList;
    private PROPERTIES propertyList;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyDevice(RESOURCES resources, CHILD_RESOURCES childResources, DeviceWrappable wrappable) {
        super(resources, childResources, wrappable);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        commandList = (COMMANDS)getWrapper(COMMANDS_ID);
        valueList = (VALUES)getWrapper(VALUES_ID);
        propertyList = (PROPERTIES)getWrapper(PROPERTIES_ID);
    }

    @Override
    public final COMMANDS getCommands() {
        return commandList;
    }

    @Override
    public final VALUES getValues() {
        return valueList;
    }

    @Override
    public final PROPERTIES getProperties() {
        return propertyList;
    }
}
