package com.intuso.housemate.object.server;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class ServerProxyDevice
        extends ServerProxyPrimaryObject<
            DeviceData,
            ServerProxyDevice,
            DeviceListener<? super ServerProxyDevice>>
        implements Device<
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyList<CommandData, ServerProxyCommand>,
            RealValue<Boolean>,
            ServerProxyValue,
            ServerProxyValue,
            ServerProxyValue,
            ServerProxyList<ValueData, ServerProxyValue>,
            ServerProxyProperty,
            ServerProxyList<PropertyData, ServerProxyProperty>,
            ServerProxyDevice> {

    private ServerProxyList<CommandData, ServerProxyCommand> commands;
    private ServerProxyList<ValueData, ServerProxyValue> values;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;
    private RealValue<Boolean> connected;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyDevice(Log log, ListenersFactory listenersFactory, Injector injector, BooleanType booleanType, @Assisted DeviceData data) {
        super(log, listenersFactory, injector, data);
        connected = new RealValue<Boolean>(log, listenersFactory, CONNECTED_ID, CONNECTED_ID,
                            "Whether the server has a connection open to control the object", booleanType, true);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (ServerProxyList<CommandData, ServerProxyCommand>) getChild(COMMANDS_ID);
        values = (ServerProxyList<ValueData, ServerProxyValue>) getChild(VALUES_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(PROPERTIES_ID);
    }

    @Override
    public ServerProxyList<CommandData, ServerProxyCommand> getCommands() {
        return commands;
    }

    @Override
    public ServerProxyList<ValueData, ServerProxyValue> getValues() {
        return values;
    }

    @Override
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    public boolean isConnected() {
        return connected.getTypedValue() != null ? connected.getTypedValue() : false;
    }

    @Override
    public RealValue<Boolean> getConnectedValue() {
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

    @Override
    public void clientContactable(boolean contactable) {
        connected.setTypedValues(contactable);
        getStartCommand().getEnabledValue().setTypedValues(contactable);
        getStopCommand().getEnabledValue().setTypedValues(contactable);
        for(ServerProxyCommand command : commands)
            command.getEnabledValue().setTypedValues(contactable);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof DeviceData)
            getProperties().copyValues(data.getChildData(PROPERTIES_ID));
    }
}
