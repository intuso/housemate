package com.intuso.housemate.object.server.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

/**
 * @param <DATA> the type of the data
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the listener
 */
public abstract class ServerRealPrimaryObject<
            DATA extends HousemateData<HousemateData<?>>,
            PRIMARY_OBJECT extends ServerRealPrimaryObject<DATA, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends ServerRealObject<DATA, HousemateData<?>, ServerRealObject<?, ?, ?, ?>, LISTENER>
        implements PrimaryObject<ServerRealCommand, ServerRealCommand,
        ServerRealValue<Boolean>, ServerRealValue<String>, PRIMARY_OBJECT, LISTENER> {

    private final ServerRealCommand remove;
    private final ServerRealValue<Boolean> connected;
    private final ServerRealValue<Boolean> running;
    private final ServerRealCommand start;
    private final ServerRealCommand stop;
    private final ServerRealValue<String> error;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     * @param objectType the name of the object type used for descriptions and logging
     */
    public ServerRealPrimaryObject(ServerRealResources resources, DATA data, final String objectType) {
        super(resources, data);
        this.remove = new ServerRealCommand(resources, REMOVE_ID, REMOVE_ID, "Remove the " + objectType, Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.connected = new ServerRealValue<Boolean>(resources, CONNECTED_ID, CONNECTED_ID, "Whether the " + objectType + " is connected or not", new BooleanType(resources.getRealResources()), true);
        this.running = new ServerRealValue<Boolean>(resources, RUNNING_ID, RUNNING_ID, "Whether the " + objectType + " is running or not", new BooleanType(resources.getRealResources()), false);
        this.start = new ServerRealCommand(resources, START_ID, START_ID, "Start the " + objectType, Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(!isRunning()) {
                    start();
                    running.setTypedValue(true);
                }
            }
        };
        this.stop = new ServerRealCommand(resources, STOP_ID, STOP_ID, "Stop the " + objectType, Lists.<ServerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning()) {
                    stop();
                    running.setTypedValue(false);
                }
            }
        };
        this.error = new ServerRealValue<String>(resources, ERROR_ID, ERROR_ID, "Current error for the " + objectType, new StringType(resources.getRealResources()), (String)null);
        addChild(this.remove);
        addChild(this.running);
        addChild(this.start);
        addChild(this.stop);
        addChild(this.error);
    }

    @Override
    public ServerRealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public ServerRealValue<String> getErrorValue() {
        return error;
    }

    @Override
    public String getError() {
        return error.getTypedValue();
    }

    @Override
    public ServerRealCommand getStopCommand() {
        return stop;
    }

    @Override
    public ServerRealCommand getStartCommand() {
        return start;
    }

    @Override
    public ServerRealValue<Boolean> getRunningValue() {
        return running;
    }

    @Override
    public boolean isRunning() {
        return running.getTypedValue() != null ? running.getTypedValue() : false;
    }

    /**
     * Removes the primary object
     */
    protected abstract void remove();

    /**
     * Starts the primary object
     * @throws HousemateException if start fails
     */
    protected abstract void start() throws HousemateException;

    /**
     * Stops the primary object
     */
    protected abstract void stop();
}
