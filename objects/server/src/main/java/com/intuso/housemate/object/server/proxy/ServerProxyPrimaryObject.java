package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the listener
 */
public class ServerProxyPrimaryObject<
            DATA extends HousemateData<HousemateData<?>>,
            PRIMARY_OBJECT extends ServerProxyPrimaryObject<DATA, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends ServerProxyObject<DATA, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, PRIMARY_OBJECT, LISTENER>
        implements PrimaryObject<ServerProxyCommand, ServerProxyCommand, ServerProxyValue,
        ServerProxyValue, PRIMARY_OBJECT, LISTENER> {

    private ServerProxyCommand remove;
    private ServerProxyValue running;
    private ServerProxyCommand start;
    private ServerProxyCommand stop;
    private ServerProxyValue error;

    /**
     * @param resources {@inheritDoc}
     * @param realResources the resources for real objects
     * @param data {@inheritDoc}
     */
    protected ServerProxyPrimaryObject(ServerProxyResources<? extends HousemateObjectFactory<ServerProxyResources<?>, HousemateData<?>, ? extends ServerProxyObject<?, ?, ?, ?, ?>>> resources, DATA data) {
        super(resources, data);
    }

    @Override
    protected void getChildObjects() {
        remove = (ServerProxyCommand) getChild(REMOVE_ID);
        running = (ServerProxyValue) getChild(RUNNING_ID);
        start = (ServerProxyCommand) getChild(START_ID);
        stop = (ServerProxyCommand) getChild(STOP_ID);
        error = (ServerProxyValue) getChild(ERROR_ID);
    }

    @Override
    public ServerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public final boolean isRunning() {
        List<Boolean> isRunnings = RealType.deserialiseAll(BooleanType.SERIALISER, running.getTypeInstances());
        return isRunnings != null && isRunnings.size() > 0 && isRunnings.get(0) != null ? isRunnings.get(0) : false;
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
    public final String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, error.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ServerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        if(running != null) {
            result.add(running.addObjectListener(new ValueListener<ServerProxyValue>() {

                @Override
                public void valueChanging(ServerProxyValue value) {
                    // do nothing
                }

                @Override
                public void valueChanged(ServerProxyValue value) {
                    for(PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<ServerProxyValue>() {

                @Override
                public void valueChanging(ServerProxyValue value) {
                    // do nothing
                }

                @Override
                public void valueChanged(ServerProxyValue value) {
                    for (PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                        listener.error(getThis(), getError());
                }
            }));
        }
        return result;
    }
}
