package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

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

    protected PrimaryObjectBridge(BrokerBridgeResources resources, WBL wrappable, PrimaryObject<?, ?, ?, ?, ?, ?, ?> proxyObject) {
        super(resources,  wrappable);
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
        Boolean value = BooleanType.SERIALISER.deserialise(connectedValue.getTypeInstance());
        return value != null ? value : false;
    }

    @Override
    public ValueBridge getConnectedValue() {
        return connectedValue;
    }

    @Override
    public boolean isRunning() {
        Boolean value = BooleanType.SERIALISER.deserialise(runningValue.getTypeInstance());
        return value != null ? value : false;
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
        return StringType.SERIALISER.deserialise(errorValue.getTypeInstance());
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }
}
