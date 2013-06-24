package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Data object for a task
 */
public final class TaskWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    private TaskWrappable() {}

    public TaskWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new TaskWrappable(getId(), getName(), getDescription());
    }
}
