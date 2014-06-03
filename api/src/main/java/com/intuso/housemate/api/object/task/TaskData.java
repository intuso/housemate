package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.HousemateData;

/**
 *
 * Data object for a task
 */
public final class TaskData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public TaskData() {}

    public TaskData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new TaskData(getId(), getName(), getDescription());
    }
}
