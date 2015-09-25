package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Device;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

import java.util.List;

public class ServerProxyDevice
        extends ServerProxyObject<DeviceData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyDevice, Device.Listener<? super ServerProxyDevice>>
        implements Device<
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyCommand,
            ServerProxyList<CommandData, ServerProxyCommand>,
            ServerProxyValue,
            ServerProxyValue,
            ServerProxyValue,
            ServerProxyList<ValueData, ServerProxyValue>,
            ServerProxyProperty,
            ServerProxyList<PropertyData, ServerProxyProperty>,
            ServerProxyDevice> {

    private ServerProxyCommand rename;
    private ServerProxyCommand remove;
    private ServerProxyValue running;
    private ServerProxyCommand start;
    private ServerProxyCommand stop;
    private ServerProxyValue error;
    private ServerProxyList<CommandData, ServerProxyCommand> commands;
    private ServerProxyList<ValueData, ServerProxyValue> values;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyDevice(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted DeviceData data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        rename = (ServerProxyCommand) getChild(DeviceData.RENAME_ID);
        remove = (ServerProxyCommand) getChild(DeviceData.REMOVE_ID);
        running = (ServerProxyValue) getChild(DeviceData.RUNNING_ID);
        start = (ServerProxyCommand) getChild(DeviceData.START_ID);
        stop = (ServerProxyCommand) getChild(DeviceData.STOP_ID);
        error = (ServerProxyValue) getChild(DeviceData.ERROR_ID);
        commands = (ServerProxyList<CommandData, ServerProxyCommand>) getChild(DeviceData.COMMANDS_ID);
        values = (ServerProxyList<ValueData, ServerProxyValue>) getChild(DeviceData.VALUES_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(DeviceData.PROPERTIES_ID);
    }

    @Override
    public ServerProxyCommand getRenameCommand() {
        return rename;
    }

    @Override
    public ServerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public ServerProxyValue getRunningValue() {
        return running;
    }

    @Override
    public ServerProxyCommand getStartCommand() {
        return start;
    }

    @Override
    public ServerProxyCommand getStopCommand() {
        return stop;
    }

    @Override
    public ServerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(DeviceData.NEW_NAME, new Message.Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) {
                String oldName = getData().getName();
                String newName = message.getPayload().getOriginal().getValue();
                getData().setName(newName);
                for(Device.Listener<? super ServerProxyDevice> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
            }
        }));
        return result;
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
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof DeviceData)
            getProperties().copyValues(data.getChildData(DeviceData.PROPERTIES_ID));
    }
}
