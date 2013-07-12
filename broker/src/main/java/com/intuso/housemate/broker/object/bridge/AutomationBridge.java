package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;

import javax.annotation.Nullable;

/**
 */
public class AutomationBridge
        extends PrimaryObjectBridge<AutomationData, AutomationBridge, AutomationListener<? super AutomationBridge>>
        implements Automation<CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge, ValueBridge,
                            ConditionBridge,ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>, TaskBridge,
                            ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge>, AutomationBridge> {

    private ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge> satisfiedTaskList;
    private ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge> unsatisfiedTaskList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedTask;
    private CommandBridge addUnsatisfiedTask;
    
    public AutomationBridge(BrokerBridgeResources resources, Automation<?, ?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?>, ?, ? extends Task<?, ?, ?, ?>, ?, ?> automation) {
        super(resources, new AutomationData(automation.getId(), automation.getName(), automation.getDescription()), automation);
        conditionList = new ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>(resources,  automation.getConditions(), new ConditionBridge.Converter(resources));
        satisfiedTaskList = new ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge>(resources, automation.getSatisfiedTasks(), new TaskBridge.Converter(resources));
        unsatisfiedTaskList = new ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge>(resources, automation.getUnsatisfiedTasks(), new TaskBridge.Converter(resources));
        addCondition = new CommandBridge(resources, automation.getAddConditionCommand());
        addSatisfiedTask = new CommandBridge(resources, automation.getAddSatisifedTaskCommand());
        addUnsatisfiedTask = new CommandBridge(resources, automation.getAddUnsatisifedTaskCommand());
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
    public ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge> getUnsatisfiedTasks() {
        return unsatisfiedTaskList;
    }

    @Override
    public ListBridge<TaskData, Task<?, ?, ?, ?>, TaskBridge> getSatisfiedTasks() {
        return satisfiedTaskList;
    }

    @Override
    public ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
        return conditionList;
    }

    public final static class Converter implements Function<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public AutomationBridge apply(@Nullable Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation) {
            return new AutomationBridge(resources, automation);
        }
    }
}
