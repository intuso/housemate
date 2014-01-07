package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for tasks
 */
public interface TaskFactory<
            T extends Task<?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<TaskData, T> {
}
