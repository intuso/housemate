package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.PropertyData;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

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
                    ServerProxyList<PropertyData, ServerProxyProperty>,
                    ServerProxyTask> {

    private ServerProxyCommand remove;
    private ServerProxyValue error;
    private ServerProxyValue executing;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyTask(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, @Assisted TaskData data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(TaskData.REMOVE_ID);
        error = (ServerProxyValue) getChild(TaskData.ERROR_ID);
        executing = (ServerProxyValue) getChild(TaskData.EXECUTING_ID);
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
