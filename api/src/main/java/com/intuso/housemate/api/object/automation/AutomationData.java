package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for an automation
 */
public final class AutomationData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    private AutomationData() {}

    public AutomationData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new AutomationData(getId(), getName(), getDescription());
    }
}
