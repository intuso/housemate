package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
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
