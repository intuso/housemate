package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

public class ServerProxyTask
        extends ServerProxyObject<
            TaskData,
            HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
        ServerProxyTask,
            TaskListener<? super ServerProxyTask>>
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
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyTask(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted TaskData data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (ServerProxyCommand) getChild(REMOVE_ID);
        error = (ServerProxyValue) getChild(ERROR_ID);
        executing = (ServerProxyValue) getChild(EXECUTING_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(PROPERTIES_ID);
    }

    @Override
    public ServerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, error.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ServerProxyValue getExecutingValue() {
        return executing;
    }

    @Override
    public boolean isExecuting() {
        List<Boolean> isExecutings = RealType.deserialiseAll(BooleanType.SERIALISER, executing.getTypeInstances());
        return isExecutings != null && isExecutings.size() > 0 && isExecutings.get(0) != null ? isExecutings.get(0) : false;
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
            getProperties().copyValues(data.getChildData(PROPERTIES_ID));
    }
}
