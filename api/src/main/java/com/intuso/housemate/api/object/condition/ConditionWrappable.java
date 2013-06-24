package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Data object for a condition
 */
public final class ConditionWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    private ConditionWrappable() {}

    public ConditionWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ConditionWrappable(getId(), getName(), getDescription());
    }
}
