package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Data object for a device
 */
public final class DeviceWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    private DeviceWrappable() {}

    public DeviceWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new DeviceWrappable(getId(), getName(), getDescription());
    }
}
