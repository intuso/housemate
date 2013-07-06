package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for tasks
 */
public interface TaskFactory<
            R extends Resources,
            T extends Task<?, ?, ?, ?>>
        extends HousemateObjectFactory<R, TaskData, T> {
}
