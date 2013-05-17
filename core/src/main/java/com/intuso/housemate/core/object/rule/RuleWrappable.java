package com.intuso.housemate.core.object.rule;

import com.intuso.housemate.core.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public final class RuleWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    private RuleWrappable() {}

    public RuleWrappable(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new RuleWrappable(getId(), getName(), getDescription());
    }
}
