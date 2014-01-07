package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for conditions
 */
public interface ConditionFactory<
            C extends Condition<?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<ConditionData, C> {
}
