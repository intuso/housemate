package com.intuso.housemate.object.broker.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/01/13
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class BrokerRealPrimaryObject<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends BrokerRealPrimaryObject<WBL, PO, L>, L extends PrimaryListener<? super PO>>
        extends BrokerRealObject<WBL, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>, L>
        implements PrimaryObject<BrokerRealProperty<String>, BrokerRealCommand, BrokerRealCommand,
            BrokerRealValue<Boolean>, BrokerRealValue<Boolean>, BrokerRealValue<String>, PO, L> {

    private final BrokerRealCommand remove;
    private final BrokerRealValue<Boolean> connected;
    private final BrokerRealValue<Boolean> running;
    private final BrokerRealCommand start;
    private final BrokerRealCommand stop;
    private final BrokerRealValue<String> error;

    public BrokerRealPrimaryObject(BrokerRealResources resources, WBL wrappable, final String objectType) {
        super(resources, wrappable);
        this.remove = new BrokerRealCommand(resources, REMOVE_COMMAND, REMOVE_COMMAND, "Remove the " + objectType, Lists.<BrokerRealArgument<?>>newArrayList()) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.connected = new BrokerRealValue<Boolean>(resources, CONNECTED_VALUE, CONNECTED_VALUE, "Whether the " + objectType + " is connected or not", new BooleanType(resources.getRealResources()), true);
        this.running = new BrokerRealValue<Boolean>(resources, RUNNING_VALUE, RUNNING_VALUE, "Whether the " + objectType + " is running or not", new BooleanType(resources.getRealResources()), false);
        this.start = new BrokerRealCommand(resources, START_COMMAND, START_COMMAND, "Start the " + objectType, Lists.<BrokerRealArgument<?>>newArrayList()) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                if(!isRunning()) {
                    start();
                    running.setTypedValue(true);
                }
            }
        };
        this.stop = new BrokerRealCommand(resources, STOP_COMMAND, STOP_COMMAND, "Stop the " + objectType, Lists.<BrokerRealArgument<?>>newArrayList()) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                if(isRunning()) {
                    stop();
                    running.setTypedValue(false);
                }
            }
        };
        this.error = new BrokerRealValue<String>(resources, ERROR_VALUE, ERROR_VALUE, "Current error for the " + objectType, new StringType(resources.getRealResources()), null);
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
        return error.getValue();
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
    public Boolean isConnected() {
        return connected.getTypedValue();
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
    public Boolean isRunning() {
        return running.getTypedValue();
    }

    protected abstract void remove();
    protected abstract void start() throws HousemateException;
    protected abstract void stop();
}
