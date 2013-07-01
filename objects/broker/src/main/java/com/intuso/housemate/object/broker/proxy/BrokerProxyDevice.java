package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.broker.real.BrokerRealValue;

public class BrokerProxyDevice
        extends BrokerProxyPrimaryObject<
            DeviceWrappable,
            BrokerProxyDevice,
            DeviceListener<? super BrokerProxyDevice>>
        implements Device<
            BrokerProxyCommand,
            BrokerProxyCommand,
            BrokerProxyCommand,
            BrokerProxyList<CommandWrappable, BrokerProxyCommand>,
            BrokerRealValue<Boolean>,
            BrokerProxyValue,
            BrokerProxyValue,
            BrokerProxyValue,
            BrokerProxyList<ValueWrappable, BrokerProxyValue>,
            BrokerProxyProperty,
            BrokerProxyList<PropertyWrappable, BrokerProxyProperty>,
            BrokerProxyDevice> {

    private BrokerProxyList<CommandWrappable, BrokerProxyCommand> commands;
    private BrokerProxyList<ValueWrappable, BrokerProxyValue> values;
    private BrokerProxyList<PropertyWrappable, BrokerProxyProperty> properties;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyDevice(BrokerProxyResources<BrokerProxyFactory.All> resources, DeviceWrappable data) {
        super(resources, resources.getBrokerRealResources(), data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (BrokerProxyList<CommandWrappable, BrokerProxyCommand>)getWrapper(COMMANDS_ID);
        values = (BrokerProxyList<ValueWrappable, BrokerProxyValue>)getWrapper(VALUES_ID);
        properties = (BrokerProxyList<PropertyWrappable, BrokerProxyProperty>)getWrapper(PROPERTIES_ID);
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
