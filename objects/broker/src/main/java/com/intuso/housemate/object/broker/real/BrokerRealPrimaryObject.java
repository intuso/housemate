package com.intuso.housemate.object.broker.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
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
public abstract class BrokerRealPrimaryObject<
            DATA extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PRIMARY_OBJECT extends BrokerRealPrimaryObject<DATA, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends BrokerRealObject<DATA, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>, LISTENER>
        implements PrimaryObject<BrokerRealCommand, BrokerRealCommand,
            BrokerRealValue<Boolean>, BrokerRealValue<Boolean>, BrokerRealValue<String>, PRIMARY_OBJECT, LISTENER> {

    private final BrokerRealCommand remove;
    private final BrokerRealValue<Boolean> connected;
    private final BrokerRealValue<Boolean> running;
    private final BrokerRealCommand start;
    private final BrokerRealCommand stop;
    private final BrokerRealValue<String> error;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     * @param objectType the name of the object type used for descriptions and logging
     */
    public BrokerRealPrimaryObject(BrokerRealResources resources, DATA data, final String objectType) {
        super(resources, data);
        this.remove = new BrokerRealCommand(resources, REMOVE_COMMAND_ID, REMOVE_COMMAND_ID, "Remove the " + objectType, Lists.<BrokerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.connected = new BrokerRealValue<Boolean>(resources, CONNECTED_VALUE_ID, CONNECTED_VALUE_ID, "Whether the " + objectType + " is connected or not", new BooleanType(resources.getRealResources()), true);
        this.running = new BrokerRealValue<Boolean>(resources, RUNNING_VALUE_ID, RUNNING_VALUE_ID, "Whether the " + objectType + " is running or not", new BooleanType(resources.getRealResources()), false);
        this.start = new BrokerRealCommand(resources, START_COMMAND_ID, START_COMMAND_ID, "Start the " + objectType, Lists.<BrokerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(!isRunning()) {
                    start();
                    running.setTypedValue(true);
                }
            }
        };
        this.stop = new BrokerRealCommand(resources, STOP_COMMAND_ID, STOP_COMMAND_ID, "Stop the " + objectType, Lists.<BrokerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning()) {
                    stop();
                    running.setTypedValue(false);
                }
            }
        };
        this.error = new BrokerRealValue<String>(resources, ERROR_VALUE_ID, ERROR_VALUE_ID, "Current error for the " + objectType, new StringType(resources.getRealResources()), null);
        addWrapper(this.remove);
        addWrapper(this.running);
        addWrapper(this.start);
        addWrapper(this.stop);
        addWrapper(this.error);
    }

    @Override
    public BrokerRealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public BrokerRealValue<String> getErrorValue() {
        return error;
    }

    @Override
    public String getError() {
        return error.getTypedValue();
    }

    @Override
    public BrokerRealCommand getStopCommand() {
        return stop;
    }

    @Override
    public BrokerRealCommand getStartCommand() {
        return start;
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
    public BrokerRealValue<Boolean> getRunningValue() {
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
