package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyDevice
        extends BrokerProxyPrimaryObject<DeviceWrappable, BrokerProxyDevice, DeviceListener<? super BrokerProxyDevice>>
        implements Device<BrokerRealCommand, BrokerProxyCommand, BrokerProxyCommand,
            BrokerProxyList<CommandWrappable, BrokerProxyCommand>, BrokerProxyValue, BrokerProxyValue, BrokerProxyValue,
            BrokerProxyList<ValueWrappable, BrokerProxyValue>, BrokerProxyProperty, BrokerProxyProperty,
            BrokerProxyList<PropertyWrappable, BrokerProxyProperty>, BrokerProxyDevice> {

    private BrokerProxyList<CommandWrappable, BrokerProxyCommand> commands;
    private BrokerProxyList<ValueWrappable, BrokerProxyValue> values;
    private BrokerProxyList<PropertyWrappable, BrokerProxyProperty> properties;

    public BrokerProxyDevice(BrokerProxyResources<BrokerProxyFactory.All> resources, DeviceWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (BrokerProxyList<CommandWrappable, BrokerProxyCommand>)getWrapper(COMMANDS);
        values = (BrokerProxyList<ValueWrappable, BrokerProxyValue>)getWrapper(VALUES);
        properties = (BrokerProxyList<PropertyWrappable, BrokerProxyProperty>)getWrapper(PROPERTIES);
    }

    @Override
    public BrokerProxyList<CommandWrappable, BrokerProxyCommand> getCommands() {
        return commands;
    }

    @Override
    public BrokerProxyList<ValueWrappable, BrokerProxyValue> getValues() {
        return values;
    }

    @Override
    public BrokerProxyList<PropertyWrappable, BrokerProxyProperty> getProperties() {
        return properties;
    }
}
