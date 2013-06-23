package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskWrappable;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/08/12
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class AutomationBridge
        extends PrimaryObjectBridge<AutomationWrappable, AutomationBridge, AutomationListener<? super AutomationBridge>>
        implements Automation<PropertyBridge, CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge, ValueBridge,
                            ConditionBridge,ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>, TaskBridge,
                            ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge>, AutomationBridge> {

    private ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge> satisfiedTaskList;
    private ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge> unsatisfiedTaskList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedTask;
    private CommandBridge addUnsatisfiedTask;
    
    public AutomationBridge(BrokerBridgeResources resources, Automation<?, ?, ?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?>, ?, ? extends Task<?, ?, ?, ?>, ?, ?> automation) {
        super(resources, new AutomationWrappable(automation.getId(), automation.getName(), automation.getDescription()), automation);
        conditionList = new ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge>(resources,  automation.getConditions(), new ConditionBridge.Converter(resources));
        satisfiedTaskList = new ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge>(resources, automation.getSatisfiedTasks(), new TaskBridge.Converter(resources));
        unsatisfiedTaskList = new ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge>(resources, automation.getUnsatisfiedTasks(), new TaskBridge.Converter(resources));
        addCondition = new CommandBridge(resources, automation.getAddConditionCommand());
        addSatisfiedTask = new CommandBridge(resources, automation.getAddSatisifedTaskCommand());
        addUnsatisfiedTask = new CommandBridge(resources, automation.getAddUnsatisifedTaskCommand());
        addWrapper(conditionList);
        addWrapper(satisfiedTaskList);
        addWrapper(unsatisfiedTaskList);
        addWrapper(addCondition);
        addWrapper(addSatisfiedTask);
        addWrapper(addUnsatisfiedTask);
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
    public ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge> getUnsatisfiedTasks() {
        return unsatisfiedTaskList;
    }

    @Override
    public ListBridge<TaskWrappable, Task<?, ?, ?, ?>, TaskBridge> getSatisfiedTasks() {
        return satisfiedTaskList;
    }

    @Override
    public ListBridge<ConditionWrappable, Condition<?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
        return conditionList;
    }

    public final static class Converter implements Function<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public AutomationBridge apply(@Nullable Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation) {
            return new AutomationBridge(resources, automation);
        }
    }
}
