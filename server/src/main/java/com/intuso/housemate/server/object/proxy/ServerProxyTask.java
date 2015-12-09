package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

public class ServerProxyTask
        extends ServerProxyObject<
        TaskData,
        HousemateData<?>,
        ServerProxyObject<?, ?, ?, ?, ?>,
        ServerProxyTask,
        Task.Listener<? super ServerProxyTask>>
        implements Task<
        ServerProxyCommand,
        ServerProxyValue,
        ServerProxyValue,
        ServerProxyProperty,
        ServerProxyValue,
        ServerProxyList<PropertyData, ServerProxyProperty>,
        ServerProxyTask> {

    private ServerProxyCommand remove;
    private ServerProxyValue error;
    private ServerProxyValue executing;
    private ServerProxyProperty driverProperty;
    private ServerProxyValue driverLoaded;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;

    /**
     * @param logger {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyTask(Logger logger, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted TaskData data) {
        super(logger, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(TaskData.REMOVE_ID);
        error = (ServerProxyValue) getChild(TaskData.ERROR_ID);
        executing = (ServerProxyValue) getChild(TaskData.EXECUTING_ID);
        driverProperty = (ServerProxyProperty) getChild(TaskData.DRIVER_ID);
        driverLoaded = (ServerProxyValue) getChild(TaskData.DRIVER_LOADED_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(TaskData.PROPERTIES_ID);
    }

    @Override
    public ServerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public ServerProxyValue getExecutingValue() {
        return executing;
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
    public ServerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof TaskData)
            getProperties().copyValues(data.getChildData(TaskData.PROPERTIES_ID));
    }
}
