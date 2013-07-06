package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.broker.real.BrokerRealValue;

public class BrokerProxyDevice
        extends BrokerProxyPrimaryObject<
        DeviceData,
            BrokerProxyDevice,
            DeviceListener<? super BrokerProxyDevice>>
        implements Device<
            BrokerProxyCommand,
            BrokerProxyCommand,
            BrokerProxyCommand,
            BrokerProxyList<CommandData, BrokerProxyCommand>,
            BrokerRealValue<Boolean>,
            BrokerProxyValue,
            BrokerProxyValue,
            BrokerProxyValue,
            BrokerProxyList<ValueData, BrokerProxyValue>,
            BrokerProxyProperty,
            BrokerProxyList<PropertyData, BrokerProxyProperty>,
            BrokerProxyDevice> {

    private BrokerProxyList<CommandData, BrokerProxyCommand> commands;
    private BrokerProxyList<ValueData, BrokerProxyValue> values;
    private BrokerProxyList<PropertyData, BrokerProxyProperty> properties;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyDevice(BrokerProxyResources<BrokerProxyFactory.All> resources, DeviceData data) {
        super(resources, resources.getBrokerRealResources(), data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (BrokerProxyList<CommandData, BrokerProxyCommand>)getWrapper(COMMANDS_ID);
        values = (BrokerProxyList<ValueData, BrokerProxyValue>)getWrapper(VALUES_ID);
        properties = (BrokerProxyList<PropertyData, BrokerProxyProperty>)getWrapper(PROPERTIES_ID);
    }

    @Override
    public BrokerProxyList<CommandData, BrokerProxyCommand> getCommands() {
        return commands;
    }

    @Override
    public BrokerProxyList<ValueData, BrokerProxyValue> getValues() {
        return values;
    }

    @Override
    public BrokerProxyList<PropertyData, BrokerProxyProperty> getProperties() {
        return properties;
    }
}
