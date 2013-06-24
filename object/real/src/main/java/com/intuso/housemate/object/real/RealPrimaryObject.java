package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

/**
 */
public abstract class RealPrimaryObject<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends RealPrimaryObject<WBL, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends RealObject<WBL, HousemateObjectWrappable<?>, RealObject<?, ?, ?, ?>, L>
        implements PrimaryObject<RealCommand, RealCommand, RealValue<Boolean>, RealValue<Boolean>, RealValue<String>, PO, L> {

    private final RealCommand remove;
    private final RealValue<Boolean> running;
    private final RealCommand start;
    private final RealCommand stop;
    private final RealValue<String> error;

    public RealPrimaryObject(RealResources resources, WBL wrappable, final String objectType) {
        super(resources, wrappable);
        this.remove = new RealCommand(resources, REMOVE_COMMAND_ID, REMOVE_COMMAND_ID, "Remove the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(resources, RUNNING_VALUE_ID, RUNNING_VALUE_ID, "Whether the " + objectType + " is running or not", false);
        this.start = new RealCommand(resources, START_COMMAND_ID, START_COMMAND_ID, "Start the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(!isRunning()) {
                    _start();
                    running.setTypedValue(true);
                }
            }
        };
        this.stop = new RealCommand(resources, STOP_COMMAND_ID, STOP_COMMAND_ID, "Stop the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(isRunning()) {
                    _stop();
                    running.setTypedValue(false);
                }
            }
        };
        this.error = StringType.createValue(resources, ERROR_VALUE_ID, ERROR_VALUE_ID, "Current error for the " + objectType, null);
        addWrapper(this.remove);
        addWrapper(this.running);
        addWrapper(this.start);
        addWrapper(this.stop);
        addWrapper(this.error);
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
    public boolean isConnected() {
        throw new HousemateRuntimeException("This value is not maintained by the client");
    }

    @Override
    public RealValue<Boolean> getConnectedValue() {
        throw new HousemateRuntimeException("This value is not maintained by the client");
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return running;
    }

    @Override
    public boolean isRunning() {
        return running.getTypedValue() != null ? running.getTypedValue() : false;
    }

    protected abstract void remove();
    protected abstract void _start() throws HousemateException;
    protected abstract void _stop();
}
