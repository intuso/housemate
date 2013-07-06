package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for conditions
 */
public interface ConditionFactory<
            R extends Resources,
            C extends Condition<?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<R, ConditionData, C> {
}
