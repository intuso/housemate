package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationListener;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.utilities.log.Log;

/**
 */
public class AutomationBridge
        extends PrimaryObjectBridge<AutomationData, AutomationBridge, AutomationListener<? super AutomationBridge>>
        implements Automation<CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge,
            ConditionBridge,ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>, TaskBridge,
            ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge>, AutomationBridge> {

    private ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> conditionList;
    private ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> satisfiedTaskList;
    private ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> unsatisfiedTaskList;
    private CommandBridge addCondition;
    private CommandBridge addSatisfiedTask;
    private CommandBridge addUnsatisfiedTask;
    
    public AutomationBridge(Log log,
                            Automation<?, ?, ?, ?, ?, ? extends Condition<?, ?, ?, ?, ?, ?, ?>, ?, ? extends Task<?, ?, ?, ?, ?>, ?, ?> automation,
                            ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, new AutomationData(automation.getId(), automation.getName(), automation.getDescription()), automation, types);
        conditionList = new ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge>(log,
                automation.getConditions(), new ConditionBridge.Converter(log, types));
        satisfiedTaskList = new ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge>(log,
                automation.getSatisfiedTasks(), new TaskBridge.Converter(log, types));
        unsatisfiedTaskList = new ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge>(log,
                automation.getUnsatisfiedTasks(), new TaskBridge.Converter(log, types));
        addCondition = new CommandBridge(log, automation.getAddConditionCommand(), types);
        addSatisfiedTask = new CommandBridge(log, automation.getAddSatisifedTaskCommand(), types);
        addUnsatisfiedTask = new CommandBridge(log, automation.getAddUnsatisifedTaskCommand(), types);
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
    public ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> getUnsatisfiedTasks() {
        return unsatisfiedTaskList;
    }

    @Override
    public ListBridge<TaskData, Task<?, ?, ?, ?, ?>, TaskBridge> getSatisfiedTasks() {
        return satisfiedTaskList;
    }

    @Override
    public ListBridge<ConditionData, Condition<?, ?, ?, ?, ?, ?, ?>, ConditionBridge> getConditions() {
        return conditionList;
    }

    public final static class Converter implements Function<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, AutomationBridge> {

        private final Log log;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.types = types;
        }

        @Override
        public AutomationBridge apply(Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> automation) {
            return new AutomationBridge(log, automation, types);
        }
    }
}
