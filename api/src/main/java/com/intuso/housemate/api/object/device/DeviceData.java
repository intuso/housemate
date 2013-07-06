package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a device
 */
public final class DeviceData extends HousemateData<HousemateData<?>> {

    private DeviceData() {}

    public DeviceData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new DeviceData(getId(), getName(), getDescription());
    }
}
