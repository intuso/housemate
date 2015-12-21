package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Automation;
import com.intuso.housemate.object.api.internal.Condition;
import com.intuso.housemate.object.api.internal.Task;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 */
public class AutomationBridge
        extends BridgeObject<AutomationData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, AutomationBridge, Automation.Listener<? super AutomationBridge>>
        implements Automation<CommandBridge, CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge,
            ConditionBridge,ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge>, TaskBridge,
        ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?, ?, ?>, TaskBridge>, AutomationBridge> {

    private Automation<?, ?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?, ? extends Task<?, ?, ?, ?, ?, ?, ?>, ?, ?> automation;
    private CommandBridge renameCommand;
    private CommandBridge removeCommand;
    private ValueBridge runningValue;
    private CommandBridge startCommand;
    private CommandBridge stopCommand;
    private ValueBridge errorValue;
    private ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?, ?, ?>, TaskBridge> satisfiedTaskList;
    private ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?, ?, ?>, TaskBridge> unsatisfiedTaskList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedTask;
    private CommandBridge addUnsatisfiedTask;
    
    public AutomationBridge(Logger logger, ListenersFactory listenersFactory,
                            Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation) {
        super(listenersFactory, logger, new AutomationData(automation.getId(), automation.getName(), automation.getDescription()));
        this.automation = automation;
        renameCommand = new CommandBridge(logger, listenersFactory, automation.getRenameCommand());
        removeCommand = new CommandBridge(logger, listenersFactory, automation.getRemoveCommand());
        runningValue = new ValueBridge(logger, listenersFactory, automation.getRunningValue());
        startCommand = new CommandBridge(logger, listenersFactory, automation.getStartCommand());
        stopCommand = new CommandBridge(logger, listenersFactory, automation.getStopCommand());
        errorValue = new ValueBridge(logger, listenersFactory, automation.getErrorValue());
        conditionList = new ConvertingListBridge<>(logger, listenersFactory,
                (com.intuso.housemate.object.api.internal.List<? extends Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>) automation.getConditions(), new ConditionBridge.Converter(logger, listenersFactory));
        satisfiedTaskList = new ConvertingListBridge<>(logger, listenersFactory,
                (com.intuso.housemate.object.api.internal.List<? extends Task<?, ?, ?, ?, ?, ?, ?>>) automation.getSatisfiedTasks(), new TaskBridge.Converter(logger, listenersFactory));
        unsatisfiedTaskList = new ConvertingListBridge<>(logger, listenersFactory,
                (com.intuso.housemate.object.api.internal.List<? extends Task<?, ?, ?, ?, ?, ?, ?>>) automation.getUnsatisfiedTasks(), new TaskBridge.Converter(logger, listenersFactory));
        addCondition = new CommandBridge(logger, listenersFactory, automation.getAddConditionCommand());
        addSatisfiedTask = new CommandBridge(logger, listenersFactory, automation.getAddSatisifedTaskCommand());
        addUnsatisfiedTask = new CommandBridge(logger, listenersFactory, automation.getAddUnsatisifedTaskCommand());
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
        addChild(conditionList);
        addChild(satisfiedTaskList);
        addChild(unsatisfiedTaskList);
        addChild(addCondition);
        addChild(addSatisfiedTask);
        addChild(addUnsatisfiedTask);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(automation.addObjectListener(new Automation.Listener<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>() {

            @Override
            public void renamed(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation, String oldName, String newName) {
                for(Automation.Listener<? super AutomationBridge> listener : getObjectListeners())
                    listener.renamed(getThis(), oldName, newName);
                getData().setName(newName);
                broadcastMessage(AutomationData.NEW_NAME, new StringPayload(newName));
            }

            @Override
            public void error(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation, String error) {

            }

            @Override
            public void running(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation, boolean running) {

            }

            @Override
            public void satisfied(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation, boolean satisfied) {

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
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public CommandBridge getAddUnsatisifedTaskCommand() {
        return addUnsatisfiedTask;
    }

    @Override
    public CommandBridge getAddSatisifedTaskCommand() {
        return addSatisfiedTask;
    }

    @Override
    public CommandBridge getAddConditionCommand() {
        return addCondition;
    }

    @Override
    public ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?, ?, ?>, TaskBridge> getUnsatisfiedTasks() {
        return unsatisfiedTaskList;
    }

    @Override
    public ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?, ?, ?>, TaskBridge> getSatisfiedTasks() {
        return satisfiedTaskList;
    }

    @Override
    public ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
        return conditionList;
    }

    public final static class Converter implements Function<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public AutomationBridge apply(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation) {
            return new AutomationBridge(logger, listenersFactory, automation);
        }
    }
}
