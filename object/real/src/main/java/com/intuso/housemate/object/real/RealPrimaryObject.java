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
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/01/13
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealPrimaryObject<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends RealPrimaryObject<WBL, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends RealObject<WBL, HousemateObjectWrappable<?>, RealObject<?, ?, ?, ?>, L>
        implements PrimaryObject<RealProperty<String>, RealCommand, RealCommand, RealValue<Boolean>, RealValue<Boolean>, RealValue<String>, PO, L> {

    private final RealCommand remove;
    private final RealValue<Boolean> running;
    private final RealCommand start;
    private final RealCommand stop;
    private final RealValue<String> error;

    public RealPrimaryObject(RealResources resources, WBL wrappable, final String objectType) {
        super(resources, wrappable);
        this.remove = new RealCommand(resources, REMOVE_COMMAND, REMOVE_COMMAND, "Remove the " + objectType, Lists.<RealArgument<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(resources, RUNNING_VALUE, RUNNING_VALUE, "Whether the " + objectType + " is running or not", false);
        this.start = new RealCommand(resources, START_COMMAND, START_COMMAND, "Start the " + objectType, Lists.<RealArgument<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(!isRunning()) {
                    _start();
                    running.setTypedValue(true);
                }
            }
        };
        this.stop = new RealCommand(resources, STOP_COMMAND, STOP_COMMAND, "Stop the " + objectType, Lists.<RealArgument<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(isRunning()) {
                    _stop();
                    running.setTypedValue(false);
                }
            }
        };
        this.error = StringType.createValue(resources, ERROR_VALUE, ERROR_VALUE, "Current error for the " + objectType, null);
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
