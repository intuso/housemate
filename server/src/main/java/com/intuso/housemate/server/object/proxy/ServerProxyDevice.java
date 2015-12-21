package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.Device;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

public class ServerProxyDevice
        extends ServerProxyObject<DeviceData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyDevice, Device.Listener<? super ServerProxyDevice>>
        implements Device<
        ServerProxyCommand,
        ServerProxyCommand,
        ServerProxyCommand,
        ServerProxyValue,
        ServerProxyValue,
        ServerProxyProperty,
        ServerProxyValue,
        ServerProxyList<PropertyData, ServerProxyProperty>,
        ServerProxyList<FeatureData, ServerProxyFeature>,
        ServerProxyDevice> {

    private ServerProxyCommand rename;
    private ServerProxyCommand remove;
    private ServerProxyValue running;
    private ServerProxyCommand start;
    private ServerProxyCommand stop;
    private ServerProxyValue error;
    private ServerProxyProperty driverProperty;
    private ServerProxyValue driverLoaded;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;
    private ServerProxyList<FeatureData, ServerProxyFeature> features;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyDevice(ListenersFactory listenersFactory,
                             ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory,
                             @Assisted Logger logger,
                             @Assisted DeviceData data) {
        super(listenersFactory, objectFactory, logger, data);
    }

    @Override
    protected void getChildObjects() {
        rename = (ServerProxyCommand) getChild(DeviceData.RENAME_ID);
        remove = (ServerProxyCommand) getChild(DeviceData.REMOVE_ID);
        running = (ServerProxyValue) getChild(DeviceData.RUNNING_ID);
        start = (ServerProxyCommand) getChild(DeviceData.START_ID);
        stop = (ServerProxyCommand) getChild(DeviceData.STOP_ID);
        error = (ServerProxyValue) getChild(DeviceData.ERROR_ID);
        driverProperty = (ServerProxyProperty) getChild(DeviceData.DRIVER_ID);
        driverLoaded = (ServerProxyValue) getChild(DeviceData.DRIVER_LOADED_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(DeviceData.PROPERTIES_ID);
        features = (ServerProxyList<FeatureData, ServerProxyFeature>) getChild(DeviceData.FEATURES_ID);
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
    public ServerProxyProperty getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ServerProxyValue getDriverLoadedValue() {
        return driverLoaded;
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
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    public ServerProxyList<FeatureData, ServerProxyFeature> getFeatures() {
        return features;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof DeviceData)
            getProperties().copyValues(data.getChildData(DeviceData.PROPERTIES_ID));
    }
}
