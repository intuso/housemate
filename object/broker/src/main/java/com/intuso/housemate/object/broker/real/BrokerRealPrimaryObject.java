package com.intuso.housemate.object.broker.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

/**
 */
public abstract class BrokerRealPrimaryObject<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends BrokerRealPrimaryObject<WBL, PO, L>, L extends PrimaryListener<? super PO>>
        extends BrokerRealObject<WBL, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>, L>
        implements PrimaryObject<BrokerRealCommand, BrokerRealCommand,
            BrokerRealValue<Boolean>, BrokerRealValue<Boolean>, BrokerRealValue<String>, PO, L> {

    private final BrokerRealCommand remove;
    private final BrokerRealValue<Boolean> connected;
    private final BrokerRealValue<Boolean> running;
    private final BrokerRealCommand start;
    private final BrokerRealCommand stop;
    private final BrokerRealValue<String> error;

    public BrokerRealPrimaryObject(BrokerRealResources resources, WBL wrappable, final String objectType) {
        super(resources, wrappable);
        this.remove = new BrokerRealCommand(resources, REMOVE_COMMAND_ID, REMOVE_COMMAND_ID, "Remove the " + objectType, Lists.<BrokerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.connected = new BrokerRealValue<Boolean>(resources, CONNECTED_VALUE_ID, CONNECTED_VALUE_ID, "Whether the " + objectType + " is connected or not", new BooleanType(resources.getRealResources()), true);
        this.running = new BrokerRealValue<Boolean>(resources, RUNNING_VALUE_ID, RUNNING_VALUE_ID, "Whether the " + objectType + " is running or not", new BooleanType(resources.getRealResources()), false);
        this.start = new BrokerRealCommand(resources, START_COMMAND_ID, START_COMMAND_ID, "Start the " + objectType, Lists.<BrokerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                if(!isRunning()) {
                    start();
                    running.setTypedValue(true);
                }
            }
        };
        this.stop = new BrokerRealCommand(resources, STOP_COMMAND_ID, STOP_COMMAND_ID, "Stop the " + objectType, Lists.<BrokerRealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
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

    protected abstract void remove();
    protected abstract void start() throws HousemateException;
    protected abstract void stop();
}
