package com.intuso.housemate.comms.api.internal.payload;

/**
 *
 * Data object for a task
 */
public final class TaskData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String REMOVE_ID = "remove";
    public final static String EXECUTING_ID = "executing";
    public final static String ERROR_ID = "error";
    public final static String PROPERTIES_ID = "properties";

    public TaskData() {}

    public TaskData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new TaskData(getId(), getName(), getDescription());
    }
}
