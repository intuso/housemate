package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.log.Log;

/**
 * @param <DATA> the type of the data object
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of this object's listener
 */
public abstract class RealPrimaryObject<
            DATA extends HousemateData<HousemateData<?>>,
            PRIMARY_OBJECT extends RealPrimaryObject<DATA, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends RealObject<DATA, HousemateData<?>, RealObject<?, ?, ?, ?>, LISTENER>
        implements PrimaryObject<RealCommand, RealCommand, RealValue<Boolean>, RealValue<String>, PRIMARY_OBJECT, LISTENER> {

    private final RealCommand remove;
    private final RealValue<Boolean> running;
    private final RealCommand start;
    private final RealCommand stop;
    private final RealValue<String> error;

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     * @param objectType the name of this object type, used child descriptions and log messages
     */
    public RealPrimaryObject(Log log, DATA data, final String objectType) {
        super(log, data);
        this.remove = new RealCommand(log, REMOVE_ID, REMOVE_ID, "Remove the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(log, RUNNING_ID, RUNNING_ID, "Whether the " + objectType + " is running or not", false);
        this.start = new RealCommand(log, START_ID, START_ID, "Start the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(!isRunning()) {
                    _start();
                    running.setTypedValues(true);
                }
            }
        };
        this.stop = new RealCommand(log, STOP_ID, STOP_ID, "Stop the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning()) {
                    _stop();
                    running.setTypedValues(false);
                }
            }
        };
        this.error = StringType.createValue(log, ERROR_ID, ERROR_ID, "Current error for the " + objectType, null);
        addChild(this.remove);
        addChild(this.running);
        addChild(this.start);
        addChild(this.stop);
        addChild(this.error);
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return error;
    }

    @Override
    public String getError() {
        return error.getTypedValue();
    }

    @Override
    public RealCommand getStopCommand() {
        return stop;
    }

    @Override
    public RealCommand getStartCommand() {
        return start;
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return running;
    }

    @Override
    public boolean isRunning() {
        return running.getTypedValue() != null ? running.getTypedValue() : false;
    }

    /**
     * Removes this objects
     */
    protected abstract void remove();

    /**
     * Starts the object
     */
    protected abstract void _start();

    /**
     * Stops the object
     */
    protected abstract void _stop();
}
