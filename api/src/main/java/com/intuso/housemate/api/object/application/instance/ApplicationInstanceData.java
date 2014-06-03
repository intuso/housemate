package com.intuso.housemate.api.object.application.instance;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for an automation
 */
public final class ApplicationInstanceData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public ApplicationInstanceData() {}

    public ApplicationInstanceData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new ApplicationInstanceData(getId(), getName(), getDescription());
    }
}
