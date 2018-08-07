package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.DeviceComponent;

/**
 * Created by tomc on 02/12/16.
 */
public class DeviceComponentMapper implements ObjectMapper<DeviceComponent.Data, com.intuso.housemate.client.api.internal.object.DeviceComponent.Data> {

    @Override
    public DeviceComponent.Data map(com.intuso.housemate.client.api.internal.object.DeviceComponent.Data data) {
        if(data == null)
            return null;
        return new DeviceComponent.Data(data.getId(), data.getName(), data.getDescription(), data.getClasses(), data.getAbilities());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.DeviceComponent.Data map(DeviceComponent.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.DeviceComponent.Data(data.getId(), data.getName(), data.getDescription(), data.getClasses(), data.getAbilities());
    }
}
