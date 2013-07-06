package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a condition
 */
public final class ConditionData extends HousemateData<HousemateData<?>> {

    private ConditionData() {}

    public ConditionData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new ConditionData(getId(), getName(), getDescription());
    }
}
