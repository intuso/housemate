package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Created by tomc on 02/12/16.
 */
public class DeviceConnectedMapper implements ObjectMapper<Device.Connected.Data, com.intuso.housemate.client.api.internal.object.Device.Connected.Data> {

    @Override
    public Device.Connected.Data map(com.intuso.housemate.client.api.internal.object.Device.Connected.Data data) {
        if(data == null)
            return null;
        return new Device.Connected.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Device.Connected.Data map(Device.Connected.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Device.Connected.Data(data.getId(), data.getName(), data.getDescription());
    }
}
