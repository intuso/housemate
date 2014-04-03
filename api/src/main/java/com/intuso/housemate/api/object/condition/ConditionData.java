package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a condition
 */
public final class ConditionData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    private ConditionData() {}

    public ConditionData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new ConditionData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ConditionData))
            return false;
        ConditionData other = (ConditionData)o;
        if(!other.getChildData(Condition.PROPERTIES_ID).equals(getChildData(Condition.PROPERTIES_ID)))
            return false;
        if(!other.getChildData(Condition.CONDITIONS_ID).equals(getChildData(Condition.CONDITIONS_ID)))
            return false;
        return true;
    }
}
