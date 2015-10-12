package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HardwareData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.object.api.internal.Hardware;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

public class ServerProxyHardware
        extends ServerProxyObject<
        HardwareData,
        HousemateData<?>,
        ServerProxyObject<?, ?, ?, ?, ?>,
        ServerProxyHardware,
        Hardware.Listener<? super ServerProxyHardware>>
        implements Hardware<
        ServerProxyCommand,
        ServerProxyCommand,
        ServerProxyCommand,
        ServerProxyValue,
        ServerProxyValue,
        ServerProxyProperty,
        ServerProxyValue,
        ServerProxyList<PropertyData, ServerProxyProperty>,
        ServerProxyHardware> {

    private ServerProxyCommand rename;
    private ServerProxyCommand remove;
    private ServerProxyValue running;
    private ServerProxyCommand start;
    private ServerProxyCommand stop;
    private ServerProxyValue error;
    private ServerProxyProperty driverProperty;
    private ServerProxyValue driverLoaded;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyHardware(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted HardwareData data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        rename = (ServerProxyCommand) getChild(HardwareData.RENAME_ID);
        remove = (ServerProxyCommand) getChild(HardwareData.REMOVE_ID);
        running = (ServerProxyValue) getChild(HardwareData.RUNNING_ID);
        start = (ServerProxyCommand) getChild(HardwareData.START_ID);
        stop = (ServerProxyCommand) getChild(HardwareData.STOP_ID);
        error = (ServerProxyValue) getChild(HardwareData.ERROR_ID);
        driverProperty = (ServerProxyProperty) getChild(HardwareData.DRIVER_ID);
        driverLoaded = (ServerProxyValue) getChild(HardwareData.DRIVER_LOADED_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(HardwareData.PROPERTIES_ID);
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
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof HardwareData)
            getProperties().copyValues(data.getChildData(HardwareData.PROPERTIES_ID));
    }
}
