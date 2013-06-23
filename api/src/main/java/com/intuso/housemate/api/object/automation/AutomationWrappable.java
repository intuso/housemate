package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public final class AutomationWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    private AutomationWrappable() {}

    public AutomationWrappable(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new AutomationWrappable(getId(), getName(), getDescription());
    }
}
