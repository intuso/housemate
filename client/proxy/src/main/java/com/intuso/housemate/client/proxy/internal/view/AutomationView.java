package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class AutomationView extends View<AutomationView> {

    private CommandView renameCommandView;
    private CommandView startCommandView;
    private CommandView stopCommandView;
    private ValueView runningValueView;
    private ValueView errorValueView;
    private CommandView removeCommandView;
    private ListView<ConditionView> conditionsView;
    private CommandView addConditionCommandView;
    private ListView<TaskView> satisfiedTasksView;
    private CommandView addSatisfiedTaskCommandView;
    private ListView<TaskView> unsatisfiedTasksView;
    private CommandView addUnsatisfiedTaskCommandView;

    public AutomationView() {}

    public AutomationView(Mode mode) {
        super(mode);
    }

    public AutomationView(CommandView renameCommandView,
                          CommandView startCommandView,
                          CommandView stopCommandView,
                          ValueView runningValueView,
                          ValueView errorValueView,
                          CommandView removeCommandView,
                          ListView<ConditionView> conditionsView,
                          CommandView addConditionCommandView,
                          ListView<TaskView> satisfiedTasksView,
                          CommandView addSatisfiedTaskCommandView,
                          ListView<TaskView> unsatisfiedTasksView,
                          CommandView addUnsatisfiedTaskCommandView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.startCommandView = startCommandView;
        this.stopCommandView = stopCommandView;
        this.runningValueView = runningValueView;
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
        this.conditionsView = conditionsView;
        this.addConditionCommandView = addConditionCommandView;
        this.satisfiedTasksView = satisfiedTasksView;
        this.addSatisfiedTaskCommandView = addSatisfiedTaskCommandView;
        this.unsatisfiedTasksView = unsatisfiedTasksView;
        this.addUnsatisfiedTaskCommandView = addUnsatisfiedTaskCommandView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public AutomationView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public CommandView getStartCommandView() {
        return startCommandView;
    }

    public AutomationView setStartCommandView(CommandView startCommandView) {
        this.startCommandView = startCommandView;
        return this;
    }

    public CommandView getStopCommandView() {
        return stopCommandView;
    }

    public AutomationView setStopCommandView(CommandView stopCommandView) {
        this.stopCommandView = stopCommandView;
        return this;
    }

    public ValueView getRunningValueView() {
        return runningValueView;
    }

    public AutomationView setRunningValueView(ValueView runningValueView) {
        this.runningValueView = runningValueView;
        return this;
    }

    public ValueView getErrorValueView() {
        return errorValueView;
    }

    public AutomationView setErrorValueView(ValueView errorValueView) {
        this.errorValueView = errorValueView;
        return this;
    }

    public CommandView getRemoveCommandView() {
        return removeCommandView;
    }

    public AutomationView setRemoveCommandView(CommandView removeCommandView) {
        this.removeCommandView = removeCommandView;
        return this;
    }

    public ListView<ConditionView> getConditionsView() {
        return conditionsView;
    }

    public AutomationView setConditionsView(ListView<ConditionView> conditionsView) {
        this.conditionsView = conditionsView;
        return this;
    }

    public CommandView getAddConditionCommandView() {
        return addConditionCommandView;
    }

    public AutomationView setAddConditionCommandView(CommandView addConditionCommandView) {
        this.addConditionCommandView = addConditionCommandView;
        return this;
    }

    public ListView<TaskView> getSatisfiedTasksView() {
        return satisfiedTasksView;
    }

    public AutomationView setSatisfiedTasksView(ListView<TaskView> satisfiedTasksView) {
        this.satisfiedTasksView = satisfiedTasksView;
        return this;
    }

    public CommandView getAddSatisfiedTaskCommandView() {
        return addSatisfiedTaskCommandView;
    }

    public AutomationView setAddSatisfiedTaskCommandView(CommandView addSatisfiedTaskCommandView) {
        this.addSatisfiedTaskCommandView = addSatisfiedTaskCommandView;
        return this;
    }

    public ListView<TaskView> getUnsatisfiedTasksView() {
        return unsatisfiedTasksView;
    }

    public AutomationView setUnsatisfiedTasksView(ListView<TaskView> unsatisfiedTasksView) {
        this.unsatisfiedTasksView = unsatisfiedTasksView;
        return this;
    }

    public CommandView getAddUnsatisfiedTaskCommandView() {
        return addUnsatisfiedTaskCommandView;
    }

    public AutomationView setAddUnsatisfiedTaskCommandView(CommandView addUnsatisfiedTaskCommandView) {
        this.addUnsatisfiedTaskCommandView = addUnsatisfiedTaskCommandView;
        return this;
    }
}
