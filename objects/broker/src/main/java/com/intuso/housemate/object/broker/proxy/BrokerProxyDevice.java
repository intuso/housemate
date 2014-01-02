package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.broker.real.BrokerRealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;

import java.util.List;

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
    private BrokerRealValue<Boolean> connected;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyDevice(BrokerProxyResources<BrokerProxyFactory.All> resources, DeviceData data) {
        super(resources, data);
        connected = new BrokerRealValue<Boolean>(resources.getBrokerRealResources(), CONNECTED_ID, CONNECTED_ID,
                "Whether the server has a connection open to control the object",
                new BooleanType(resources.getRealResources()), true);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (BrokerProxyList<CommandData, BrokerProxyCommand>) getChild(COMMANDS_ID);
        values = (BrokerProxyList<ValueData, BrokerProxyValue>) getChild(VALUES_ID);
        properties = (BrokerProxyList<PropertyData, BrokerProxyProperty>) getChild(PROPERTIES_ID);
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

    @Override
    public boolean isConnected() {
        return connected.getTypedValue() != null ? connected.getTypedValue() : false;
    }

    @Override
    public BrokerRealValue<Boolean> getConnectedValue() {
        return connected;
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
}
