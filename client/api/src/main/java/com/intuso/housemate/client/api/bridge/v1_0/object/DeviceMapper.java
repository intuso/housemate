package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Created by tomc on 02/12/16.
 */
public class DeviceMapper implements ObjectMapper<Device.Data, com.intuso.housemate.client.api.internal.object.Device.Data> {

    @Override
    public Device.Data map(com.intuso.housemate.client.api.internal.object.Device.Data data) {
        if(data == null)
            return null;
        return new Device.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Device.Data map(Device.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Device.Data(data.getId(), data.getName(), data.getDescription());
    }
}
