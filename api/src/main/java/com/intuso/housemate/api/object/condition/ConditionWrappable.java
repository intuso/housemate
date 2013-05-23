package com.intuso.housemate.api.object.condition;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
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
