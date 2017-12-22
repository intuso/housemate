package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class AutomationView extends View {

    private CommandView renameCommand;
    private CommandView startCommand;
    private CommandView stopCommand;
    private ValueView runningValue;
    private ValueView errorValue;
    private CommandView removeCommand;
    private ListView<ConditionView> conditions;
    private CommandView addConditionCommand;
    private ListView<TaskView> satisfiedTasks;
    private CommandView addSatisfiedTaskCommand;
    private ListView<TaskView> unsatisfiedTasks;
    private CommandView addUnsatisfiedTaskCommand;

    public AutomationView() {}

    public AutomationView(Mode mode) {
        super(mode);
    }

    public AutomationView(CommandView renameCommand,
                          CommandView startCommand,
                          CommandView stopCommand,
                          ValueView runningValue,
                          ValueView errorValue,
                          CommandView removeCommand,
                          ListView<ConditionView> conditions,
                          CommandView addConditionCommand,
                          ListView<TaskView> satisfiedTasks,
                          CommandView addSatisfiedTaskCommand,
                          ListView<TaskView> unsatisfiedTasks,
                          CommandView addUnsatisfiedTaskCommand) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.startCommand = startCommand;
        this.stopCommand = stopCommand;
        this.runningValue = runningValue;
        this.errorValue = errorValue;
        this.removeCommand = removeCommand;
        this.conditions = conditions;
        this.addConditionCommand = addConditionCommand;
        this.satisfiedTasks = satisfiedTasks;
        this.addSatisfiedTaskCommand = addSatisfiedTaskCommand;
        this.unsatisfiedTasks = unsatisfiedTasks;
        this.addUnsatisfiedTaskCommand = addUnsatisfiedTaskCommand;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public AutomationView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public CommandView getStartCommand() {
        return startCommand;
    }

    public AutomationView setStartCommand(CommandView startCommand) {
        this.startCommand = startCommand;
        return this;
    }

    public CommandView getStopCommand() {
        return stopCommand;
    }

    public AutomationView setStopCommand(CommandView stopCommand) {
        this.stopCommand = stopCommand;
        return this;
    }

    public ValueView getRunningValue() {
        return runningValue;
    }

    public AutomationView setRunningValue(ValueView runningValue) {
        this.runningValue = runningValue;
        return this;
    }

    public ValueView getErrorValue() {
        return errorValue;
    }

    public AutomationView setErrorValue(ValueView errorValue) {
        this.errorValue = errorValue;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public AutomationView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }

    public ListView<ConditionView> getConditions() {
        return conditions;
    }

    public AutomationView setConditions(ListView<ConditionView> conditions) {
        this.conditions = conditions;
        return this;
    }

    public CommandView getAddConditionCommand() {
        return addConditionCommand;
    }

    public AutomationView setAddConditionCommand(CommandView addConditionCommand) {
        this.addConditionCommand = addConditionCommand;
        return this;
    }

    public ListView<TaskView> getSatisfiedTasks() {
        return satisfiedTasks;
    }

    public AutomationView setSatisfiedTasks(ListView<TaskView> satisfiedTasks) {
        this.satisfiedTasks = satisfiedTasks;
        return this;
    }

    public CommandView getAddSatisfiedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    public AutomationView setAddSatisfiedTaskCommand(CommandView addSatisfiedTaskCommand) {
        this.addSatisfiedTaskCommand = addSatisfiedTaskCommand;
        return this;
    }

    public ListView<TaskView> getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    public AutomationView setUnsatisfiedTasks(ListView<TaskView> unsatisfiedTasks) {
        this.unsatisfiedTasks = unsatisfiedTasks;
        return this;
    }

    public CommandView getAddUnsatisfiedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    public AutomationView setAddUnsatisfiedTaskCommand(CommandView addUnsatisfiedTaskCommand) {
        this.addUnsatisfiedTaskCommand = addUnsatisfiedTaskCommand;
        return this;
    }
}
