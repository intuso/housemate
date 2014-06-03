package com.intuso.housemate.api.object.application;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for an automation
 */
public final class ApplicationData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public ApplicationData() {}

    public ApplicationData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new ApplicationData(getId(), getName(), getDescription());
    }
}
