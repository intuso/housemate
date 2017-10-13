package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.*;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.*;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the add command
 * @param <VALUE> the type of the value
 * @param <CONDITIONS> the type of the conditions list
 * @param <TASKS> the type of the tasks list
 * @param <AUTOMATION> the type of the automation
 */
public abstract class ProxyAutomation<
        COMMAND extends ProxyCommand<?, ?, ?>,
        VALUE extends ProxyValue<?, ?>,
        CONDITIONS extends ProxyList<? extends ProxyCondition<?, ?, ?, ?, ?, ?>, ?>,
        TASKS extends ProxyList<? extends ProxyTask<?, ?, ?, ?, ?>, ?>,
        AUTOMATION extends ProxyAutomation<COMMAND, VALUE, CONDITIONS, TASKS, AUTOMATION>>
        extends ProxyObject<Automation.Data, Automation.Listener<? super AUTOMATION>, AutomationView>
        implements Automation<COMMAND, COMMAND, VALUE, COMMAND, VALUE, COMMAND, CONDITIONS, TASKS, AUTOMATION>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyRenameable<COMMAND>,
        ProxyRunnable<COMMAND, VALUE> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<VALUE> valueFactory;
    private final ProxyObject.Factory<CONDITIONS> conditionsFactory;
    private final ProxyObject.Factory<TASKS> tasksFactory;

    private COMMAND renameCommand;
    private COMMAND removeCommand;
    private VALUE runningValue;
    private COMMAND startCommand;
    private COMMAND stopCommand;
    private VALUE errorValue;
    private CONDITIONS conditions;
    private COMMAND addConditionCommand;
    private TASKS satisfiedTasks;
    private COMMAND addSatisfiedTaskCommand;
    private TASKS unsatisfiedTasks;
    private COMMAND addUnsatisfiedTaskCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyAutomation(Logger logger,
                           String name,
                           ManagedCollectionFactory managedCollectionFactory,
                           Receiver.Factory receiverFactory,
                           ProxyObject.Factory<COMMAND> commandFactory,
                           ProxyObject.Factory<VALUE> valueFactory,
                           ProxyObject.Factory<CONDITIONS> conditionsFactory,
                           ProxyObject.Factory<TASKS> tasksFactory) {
        super(logger, name, Automation.Data.class, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.valueFactory = valueFactory;
        this.conditionsFactory = conditionsFactory;
        this.tasksFactory = tasksFactory;
    }

    @Override
    public AutomationView createView(View.Mode mode) {
        return new AutomationView(mode);
    }

    @Override
    public Tree getTree(AutomationView view) {

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(RUNNING_ID, runningValue.getTree(new ValueView(View.Mode.ANCESTORS)));
                    result.getChildren().put(START_ID, startCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(STOP_ID, stopCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(new ValueView(View.Mode.ANCESTORS)));
                    result.getChildren().put(CONDITIONS_ID, conditions.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ADD_CONDITION_ID, addConditionCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(SATISFIED_TASKS_ID, satisfiedTasks.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ADD_SATISFIED_TASK_ID, addSatisfiedTaskCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(UNSATISFIED_TASKS_ID, unsatisfiedTasks.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ADD_UNSATISFIED_TASK_ID, addUnsatisfiedTaskCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommandView()));
                    result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommandView()));
                    result.getChildren().put(RUNNING_ID, runningValue.getTree(view.getRunningValueView()));
                    result.getChildren().put(START_ID, startCommand.getTree(view.getStartCommandView()));
                    result.getChildren().put(STOP_ID, stopCommand.getTree(view.getStopCommandView()));
                    result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValueView()));
                    result.getChildren().put(CONDITIONS_ID, conditions.getTree(view.getConditionsView()));
                    result.getChildren().put(ADD_CONDITION_ID, addConditionCommand.getTree(view.getAddConditionCommandView()));
                    result.getChildren().put(SATISFIED_TASKS_ID, satisfiedTasks.getTree(view.getSatisfiedTasksView()));
                    result.getChildren().put(ADD_SATISFIED_TASK_ID, addSatisfiedTaskCommand.getTree(view.getAddSatisfiedTaskCommandView()));
                    result.getChildren().put(UNSATISFIED_TASKS_ID, unsatisfiedTasks.getTree(view.getUnsatisfiedTasksView()));
                    result.getChildren().put(ADD_UNSATISFIED_TASK_ID, addUnsatisfiedTaskCommand.getTree(view.getAddUnsatisfiedTaskCommandView()));
                    break;

                case SELECTION:
                    if(view.getRenameCommandView() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommandView()));
                    if(view.getRemoveCommandView() != null)
                        result.getChildren().put(REMOVE_ID, removeCommand.getTree(view.getRemoveCommandView()));
                    if(view.getRunningValueView() != null)
                        result.getChildren().put(RUNNING_ID, runningValue.getTree(view.getRunningValueView()));
                    if(view.getStartCommandView() != null)
                        result.getChildren().put(START_ID, startCommand.getTree(view.getStartCommandView()));
                    if(view.getStopCommandView() != null)
                        result.getChildren().put(STOP_ID, stopCommand.getTree(view.getStopCommandView()));
                    if(view.getErrorValueView() != null)
                        result.getChildren().put(ERROR_ID, errorValue.getTree(view.getErrorValueView()));
                    if(view.getConditionsView() != null)
                        result.getChildren().put(CONDITIONS_ID, conditions.getTree(view.getConditionsView()));
                    if(view.getAddConditionCommandView() != null)
                        result.getChildren().put(ADD_CONDITION_ID, addConditionCommand.getTree(view.getAddConditionCommandView()));
                    if(view.getSatisfiedTasksView() != null)
                        result.getChildren().put(SATISFIED_TASKS_ID, satisfiedTasks.getTree(view.getSatisfiedTasksView()));
                    if(view.getAddSatisfiedTaskCommandView() != null)
                        result.getChildren().put(ADD_SATISFIED_TASK_ID, addSatisfiedTaskCommand.getTree(view.getAddSatisfiedTaskCommandView()));
                    if(view.getUnsatisfiedTasksView() != null)
                        result.getChildren().put(UNSATISFIED_TASKS_ID, unsatisfiedTasks.getTree(view.getUnsatisfiedTasksView()));
                    if(view.getAddUnsatisfiedTaskCommandView() != null)
                        result.getChildren().put(ADD_UNSATISFIED_TASK_ID, addUnsatisfiedTaskCommand.getTree(view.getAddUnsatisfiedTaskCommandView()));
                    break;
            }

        }

        return result;
    }

    @Override
    public void load(AutomationView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {

            // load anything that isn't already
            case ANCESTORS:
            case CHILDREN:
                if(renameCommand == null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(runningValue == null)
                    runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID), ChildUtil.name(name, RUNNING_ID));
                if(startCommand == null)
                    startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
                if(stopCommand == null)
                    stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
                if(errorValue == null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(conditions == null)
                    conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID), ChildUtil.name(name, CONDITIONS_ID));
                if(addConditionCommand == null)
                    addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID), ChildUtil.name(name, ADD_CONDITION_ID));
                if(satisfiedTasks == null)
                    satisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, SATISFIED_TASKS_ID), ChildUtil.name(name, SATISFIED_TASKS_ID));
                if(addSatisfiedTaskCommand == null)
                    addSatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, ADD_SATISFIED_TASK_ID), ChildUtil.name(name, ADD_SATISFIED_TASK_ID));
                if(unsatisfiedTasks == null)
                    unsatisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, UNSATISFIED_TASKS_ID), ChildUtil.name(name, UNSATISFIED_TASKS_ID));
                if(addUnsatisfiedTaskCommand == null)
                    addUnsatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, ADD_UNSATISFIED_TASK_ID), ChildUtil.name(name, ADD_UNSATISFIED_TASK_ID));
                break;

            // load whatever the view needs
            case SELECTION:
                if(renameCommand == null && view.getRenameCommandView() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(removeCommand == null && view.getRemoveCommandView() != null)
                    removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
                if(runningValue == null && view.getRunningValueView() != null)
                    runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID), ChildUtil.name(name, RUNNING_ID));
                if(startCommand == null && view.getStartCommandView() != null)
                    startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
                if(stopCommand == null && view.getStopCommandView() != null)
                    stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
                if(errorValue == null && view.getErrorValueView() != null)
                    errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
                if(conditions == null && view.getConditionsView() != null)
                    conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID), ChildUtil.name(name, CONDITIONS_ID));
                if(addConditionCommand == null && view.getAddConditionCommandView() != null)
                    addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID), ChildUtil.name(name, ADD_CONDITION_ID));
                if(satisfiedTasks == null && view.getSatisfiedTasksView() != null)
                    satisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, SATISFIED_TASKS_ID), ChildUtil.name(name, SATISFIED_TASKS_ID));
                if(addSatisfiedTaskCommand == null && view.getAddSatisfiedTaskCommandView() != null)
                    addSatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, ADD_SATISFIED_TASK_ID), ChildUtil.name(name, ADD_SATISFIED_TASK_ID));
                if(unsatisfiedTasks == null && view.getUnsatisfiedTasksView() != null)
                    unsatisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, UNSATISFIED_TASKS_ID), ChildUtil.name(name, UNSATISFIED_TASKS_ID));
                if(addUnsatisfiedTaskCommand == null && view.getAddUnsatisfiedTaskCommandView() != null)
                    addUnsatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, ADD_UNSATISFIED_TASK_ID), ChildUtil.name(name, ADD_UNSATISFIED_TASK_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {

            // view recursively. Ignore any specific view
            case ANCESTORS:
                renameCommand.load(new CommandView(View.Mode.ANCESTORS));
                removeCommand.load(new CommandView(View.Mode.ANCESTORS));
                runningValue.load(new ValueView(View.Mode.ANCESTORS));
                startCommand.load(new CommandView(View.Mode.ANCESTORS));
                stopCommand.load(new CommandView(View.Mode.ANCESTORS));
                errorValue.load(new ValueView(View.Mode.ANCESTORS));
                conditions.load(new ListView(View.Mode.ANCESTORS));
                addConditionCommand.load(new CommandView(View.Mode.ANCESTORS));
                satisfiedTasks.load(new ListView(View.Mode.ANCESTORS));
                addSatisfiedTaskCommand.load(new CommandView(View.Mode.ANCESTORS));
                unsatisfiedTasks.load(new ListView(View.Mode.ANCESTORS));
                addUnsatisfiedTaskCommand.load(new CommandView(View.Mode.ANCESTORS));
                break;

            // load using the view's nested views
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommandView() != null)
                    renameCommand.load(view.getRenameCommandView());
                if(view.getRemoveCommandView() != null)
                    removeCommand.load(view.getRemoveCommandView());
                if(view.getRunningValueView() != null)
                    runningValue.load(view.getRunningValueView());
                if(view.getStartCommandView() != null)
                    startCommand.load(view.getStartCommandView());
                if(view.getStopCommandView() != null)
                    stopCommand.load(view.getStopCommandView());
                if(view.getErrorValueView() != null)
                    errorValue.load(view.getErrorValueView());
                if(view.getConditionsView() != null)
                    conditions.load(view.getConditionsView());
                if(view.getAddConditionCommandView() != null)
                    addConditionCommand.load(view.getAddConditionCommandView());
                if(view.getSatisfiedTasksView() != null)
                    satisfiedTasks.load(view.getSatisfiedTasksView());
                if(view.getAddSatisfiedTaskCommandView() != null)
                    addSatisfiedTaskCommand.load(view.getAddSatisfiedTaskCommandView());
                if(view.getUnsatisfiedTasksView() != null)
                    unsatisfiedTasks.load(view.getUnsatisfiedTasksView());
                if(view.getAddUnsatisfiedTaskCommandView() != null)
                    addUnsatisfiedTaskCommand.load(view.getAddUnsatisfiedTaskCommandView());
                break;
        }
    }

    @Override
    public void loadRemoveCommand(CommandView commandView) {
        if(removeCommand == null)
            removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
        removeCommand.load(commandView);
    }

    @Override
    public void loadRenameCommand(CommandView commandView) {
        if(renameCommand == null)
            renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
        renameCommand.load(commandView);
    }

    @Override
    public void loadStartCommand(CommandView commandView) {
        if(startCommand == null)
            startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
        startCommand.load(commandView);
    }

    @Override
    public void loadStopCommand(CommandView commandView) {
        if(stopCommand == null)
            stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
        stopCommand.load(commandView);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(renameCommand != null)
            renameCommand.uninit();
        if(removeCommand != null)
            removeCommand.uninit();
        if(runningValue != null)
            runningValue.uninit();
        if(startCommand != null)
            startCommand.uninit();
        if(stopCommand != null)
            stopCommand.uninit();
        if(errorValue != null)
            errorValue.uninit();
        if(conditions != null)
            conditions.uninit();
        if(addConditionCommand != null)
            addConditionCommand.uninit();
        if(satisfiedTasks != null)
            satisfiedTasks.uninit();
        if(addSatisfiedTaskCommand != null)
            addSatisfiedTaskCommand.uninit();
        if(unsatisfiedTasks != null)
            unsatisfiedTasks.uninit();
        if(addUnsatisfiedTaskCommand != null)
            addUnsatisfiedTaskCommand.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final boolean isRunning() {
        return runningValue != null && runningValue.getValue() != null
                && runningValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(runningValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getRunningValue() {
        return runningValue;
    }

    @Override
    public COMMAND getStartCommand() {
        return startCommand;
    }

    @Override
    public COMMAND getStopCommand() {
        return stopCommand;
    }

    @Override
    public final String getError() {
        return errorValue != null && errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public CONDITIONS getConditions() {
        return conditions;
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public TASKS getSatisfiedTasks() {
        return satisfiedTasks;
    }

    @Override
    public COMMAND getAddSatisfiedTaskCommand() {
        return addSatisfiedTaskCommand;
    }

    @Override
    public TASKS getUnsatisfiedTasks() {
        return unsatisfiedTasks;
    }

    @Override
    public COMMAND getAddUnsatisfiedTaskCommand() {
        return addUnsatisfiedTaskCommand;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(REMOVE_ID.equals(id)) {
            if(removeCommand == null)
                removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID), ChildUtil.name(name, REMOVE_ID));
            return removeCommand;
        } else if(RUNNING_ID.equals(id)) {
            if(runningValue == null)
                runningValue = valueFactory.create(ChildUtil.logger(logger, RUNNING_ID), ChildUtil.name(name, RUNNING_ID));
            return runningValue;
        } else if(START_ID.equals(id)) {
            if(startCommand == null)
                startCommand = commandFactory.create(ChildUtil.logger(logger, START_ID), ChildUtil.name(name, START_ID));
            return startCommand;
        } else if(STOP_ID.equals(id)) {
            if(stopCommand == null)
                stopCommand = commandFactory.create(ChildUtil.logger(logger, STOP_ID), ChildUtil.name(name, STOP_ID));
            return stopCommand;
        } else if(ERROR_ID.equals(id)) {
            if(errorValue == null)
                errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID), ChildUtil.name(name, ERROR_ID));
            return errorValue;
        } else if(CONDITIONS_ID.equals(id)) {
            if(conditions == null)
                conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID), ChildUtil.name(name, CONDITIONS_ID));
            return conditions;
        } else if(ADD_CONDITION_ID.equals(id)) {
            if(addConditionCommand == null)
                addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID), ChildUtil.name(name, ADD_CONDITION_ID));
            return addConditionCommand;
        } else if(SATISFIED_TASKS_ID.equals(id)) {
            if(satisfiedTasks == null)
                satisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, SATISFIED_TASKS_ID), ChildUtil.name(name, SATISFIED_TASKS_ID));
            return satisfiedTasks;
        } else if(ADD_SATISFIED_TASK_ID.equals(id)) {
            if(addSatisfiedTaskCommand == null)
                addSatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, ADD_SATISFIED_TASK_ID), ChildUtil.name(name, ADD_SATISFIED_TASK_ID));
            return addSatisfiedTaskCommand;
        } else if(UNSATISFIED_TASKS_ID.equals(id)) {
            if(unsatisfiedTasks == null)
                unsatisfiedTasks = tasksFactory.create(ChildUtil.logger(logger, UNSATISFIED_TASKS_ID), ChildUtil.name(name, UNSATISFIED_TASKS_ID));
            return unsatisfiedTasks;
        } else if(ADD_UNSATISFIED_TASK_ID.equals(id)) {
            if(addUnsatisfiedTaskCommand == null)
                addUnsatisfiedTaskCommand = commandFactory.create(ChildUtil.logger(logger, ADD_UNSATISFIED_TASK_ID), ChildUtil.name(name, ADD_UNSATISFIED_TASK_ID));
            return addUnsatisfiedTaskCommand;
        }
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:15
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyAutomation<
            ProxyCommand.Simple,
            ProxyValue.Simple,
            ProxyList.Simple<ProxyCondition.Simple>,
            ProxyList.Simple<ProxyTask.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyList.Simple<ProxyCondition.Simple>> conditionsFactory,
                      Factory<ProxyList.Simple<ProxyTask.Simple>> tasksFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory, valueFactory, conditionsFactory, tasksFactory);
        }
    }
}
