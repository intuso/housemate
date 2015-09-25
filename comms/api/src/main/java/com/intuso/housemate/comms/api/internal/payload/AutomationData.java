package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for an automation
 */
public final class AutomationData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String ERROR_ID = "error";
    public final static String RENAME_ID = "rename";
    public final static String NAME_ID = "name";
    public final static String NEW_NAME = "new-name";
    public final static String RUNNING_ID = "running";
    public final static String START_ID = "start";
    public final static String STOP_ID = "stop";
    public final static String REMOVE_ID = "remove";
    public final static String CONDITIONS_ID = "conditions";
    public final static String SATISFIED_TASKS_ID = "satisfied-tasks";
    public final static String UNSATISFIED_TASKS_ID = "unsatisfied-tasks";
    public final static String ADD_CONDITION_ID = "add-condition";
    public final static String ADD_SATISFIED_TASK_ID = "add-satisfied-task";
    public final static String ADD_UNSATISFIED_TASK_ID = "add-unsatisfied-task";

    private AutomationData() {}

    public AutomationData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new AutomationData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof AutomationData))
            return false;
        AutomationData other = (AutomationData)o;
        if(!other.getChildData(CONDITIONS_ID).equals(getChildData(CONDITIONS_ID)))
            return false;
        if(!other.getChildData(SATISFIED_TASKS_ID).equals(getChildData(SATISFIED_TASKS_ID)))
            return false;
        if(!other.getChildData(UNSATISFIED_TASKS_ID).equals(getChildData(UNSATISFIED_TASKS_ID)))
            return false;
        return true;
    }
}
