package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.util.List;

/**
 */
public abstract class PrimaryObjectBridge<WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PO extends PrimaryObjectBridge<WBL, PO, L>, L extends PrimaryListener<? super PO>>
        extends BridgeObject<WBL, HousemateObjectWrappable<?>, BridgeObject<?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<CommandBridge, CommandBridge, ValueBridge, ValueBridge, ValueBridge, PO, L> {

    private CommandBridge removeCommand;
    private ValueBridge connectedValue;
    private ValueBridge runningValue;
    private CommandBridge startCommand;
    private CommandBridge stopCommand;
    private ValueBridge errorValue;

    protected PrimaryObjectBridge(BrokerBridgeResources resources, WBL data, PrimaryObject<?, ?, ?, ?, ?, ?, ?> proxyObject) {
        super(resources,  data);
        removeCommand = new CommandBridge(resources, proxyObject.getRemoveCommand());
        connectedValue = new ValueBridge(resources, proxyObject.getConnectedValue());
        runningValue = new ValueBridge(resources, proxyObject.getRunningValue());
        startCommand = new CommandBridge(resources, proxyObject.getStartCommand());
        stopCommand = new CommandBridge(resources, proxyObject.getStopCommand());
        errorValue = new ValueBridge(resources, proxyObject.getErrorValue());
        addWrapper(removeCommand);
        addWrapper(connectedValue);
        addWrapper(runningValue);
        addWrapper(startCommand);
        addWrapper(stopCommand);
        addWrapper(errorValue);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public boolean isConnected() {
        List<Boolean> connecteds = RealType.deserialiseAll(BooleanType.SERIALISER, connectedValue.getTypeInstances());
        return connecteds != null && connecteds.size() > 0 && connecteds.get(0) != null ? connecteds.get(0) : false;
    }

    @Override
    public ValueBridge getConnectedValue() {
        return connectedValue;
    }

    @Override
    public boolean isRunning() {
        List<Boolean> isRunnings = RealType.deserialiseAll(BooleanType.SERIALISER, runningValue.getTypeInstances());
        return isRunnings != null && isRunnings.size() > 0 && isRunnings.get(0) != null ? isRunnings.get(0) : false;
    }

    @Override
    public ValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public CommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public CommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, errorValue.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }
}
