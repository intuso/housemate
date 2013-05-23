package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
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
