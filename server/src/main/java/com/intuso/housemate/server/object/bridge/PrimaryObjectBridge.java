package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public abstract class PrimaryObjectBridge<WBL extends HousemateData<HousemateData<?>>,
            PO extends PrimaryObjectBridge<WBL, PO, L>, L extends PrimaryListener<? super PO>>
        extends BridgeObject<WBL, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge, PO, L> {

    private final PrimaryObject proxyObject;
    private CommandBridge renameCommand;
    private CommandBridge removeCommand;
    private ValueBridge runningValue;
    private CommandBridge startCommand;
    private CommandBridge stopCommand;
    private ValueBridge errorValue;

    protected PrimaryObjectBridge(Log log, ListenersFactory listenersFactory, WBL data, PrimaryObject<?, ?, ?, ?, ?, ?, ?> proxyObject) {
        super(log, listenersFactory, data);
        this.proxyObject = proxyObject;
        renameCommand = new CommandBridge(log, listenersFactory, proxyObject.getRenameCommand());
        removeCommand = new CommandBridge(log, listenersFactory, proxyObject.getRemoveCommand());
        runningValue = new ValueBridge(log, listenersFactory, proxyObject.getRunningValue());
        startCommand = new CommandBridge(log, listenersFactory, proxyObject.getStartCommand());
        stopCommand = new CommandBridge(log, listenersFactory, proxyObject.getStopCommand());
        errorValue = new ValueBridge(log, listenersFactory, proxyObject.getErrorValue());
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(proxyObject.addObjectListener(new PrimaryListener<PrimaryObject<?, ?, ?, ?, ?, ?, ?>>() {
                @Override
                public void renamed(PrimaryObject<?, ?, ?, ?, ?, ?, ?> primaryObject, String oldName, String newName) {
                    for(L listener : getObjectListeners())
                        listener.renamed(getThis(), oldName, newName);
                    getData().setName(newName);
                    broadcastMessage(NEW_NAME, new StringPayload(newName));
                }

                @Override
                public void error(PrimaryObject<?, ?, ?, ?, ?, ?, ?> primaryObject, String error) {

                }

                @Override
                public void running(PrimaryObject<?, ?, ?, ?, ?, ?, ?> primaryObject, boolean running) {

                }
            }));
        return result;
    }

    @Override
    public CommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
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
