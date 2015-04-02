package com.intuso.housemate.api.object.realclient;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a command
 */
public final class RealClientData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public RealClientData() {}

    public RealClientData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new RealClientData(getId(), getName(), getDescription());
    }
}
