package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class AutomationBridge
        extends PrimaryObjectBridge<AutomationData, AutomationBridge, AutomationListener<? super AutomationBridge>>
        implements Automation<CommandBridge, CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge,
            ConditionBridge,ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>, TaskBridge,
        ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge>, AutomationBridge> {

    private ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> satisfiedTaskList;
    private ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> unsatisfiedTaskList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedTask;
    private CommandBridge addUnsatisfiedTask;
    
    public AutomationBridge(Log log, ListenersFactory listenersFactory,
                            Automation<?, ?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?, ?>, ?, ? extends Task<?, ?, ?, ?, ?>, ?, ?> automation) {
        super(log, listenersFactory, new AutomationData(automation.getId(), automation.getName(), automation.getDescription()), automation);
        conditionList = new ConvertingListBridge<>(log, listenersFactory,
                automation.getConditions(), new ConditionBridge.Converter(log, listenersFactory));
        satisfiedTaskList = new ConvertingListBridge<>(log, listenersFactory,
                automation.getSatisfiedTasks(), new TaskBridge.Converter(log, listenersFactory));
        unsatisfiedTaskList = new ConvertingListBridge<>(log, listenersFactory,
                automation.getUnsatisfiedTasks(), new TaskBridge.Converter(log, listenersFactory));
        addCondition = new CommandBridge(log, listenersFactory, automation.getAddConditionCommand());
        addSatisfiedTask = new CommandBridge(log, listenersFactory, automation.getAddSatisifedTaskCommand());
        addUnsatisfiedTask = new CommandBridge(log, listenersFactory, automation.getAddUnsatisifedTaskCommand());
        addChild(conditionList);
        addChild(satisfiedTaskList);
        addChild(unsatisfiedTaskList);
        addChild(addCondition);
        addChild(addSatisfiedTask);
        addChild(addUnsatisfiedTask);
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
    public ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> getUnsatisfiedTasks() {
        return unsatisfiedTaskList;
    }

    @Override
    public ConvertingListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> getSatisfiedTasks() {
        return satisfiedTaskList;
    }

    @Override
    public ConvertingListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
        return conditionList;
    }

    public final static class Converter implements Function<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public AutomationBridge apply(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation) {
            return new AutomationBridge(log, listenersFactory, automation);
        }
    }
}
