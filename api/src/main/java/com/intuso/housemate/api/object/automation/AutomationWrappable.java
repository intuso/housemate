package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Data object for an automation
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
