package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Created by tomc on 02/12/16.
 */
public class DeviceGroupMapper implements ObjectMapper<Device.Group.Data, com.intuso.housemate.client.api.internal.object.Device.Group.Data> {

    @Override
    public Device.Group.Data map(com.intuso.housemate.client.api.internal.object.Device.Group.Data data) {
        if(data == null)
            return null;
        return new Device.Group.Data(data.getId(), data.getName(), data.getDescription(), data.getClasses(), data.getAbilities());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Device.Group.Data map(Device.Group.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Device.Group.Data(data.getId(), data.getName(), data.getDescription(), data.getClasses(), data.getAbilities());
    }
}
