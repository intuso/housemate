package com.intuso.housemate.api.object.server;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a command
 */
public final class ServerData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public ServerData() {}

    public ServerData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new ServerData(getId(), getName(), getDescription());
    }
}
