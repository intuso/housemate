package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Device;

/**
 * Created by tomc on 02/12/16.
 */
public class DeviceCombiMapper implements ObjectMapper<Device.Combi.Data, com.intuso.housemate.client.api.internal.object.Device.Combi.Data> {

    @Override
    public Device.Combi.Data map(com.intuso.housemate.client.api.internal.object.Device.Combi.Data data) {
        if(data == null)
            return null;
        return new Device.Combi.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Device.Combi.Data map(Device.Combi.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Device.Combi.Data(data.getId(), data.getName(), data.getDescription());
    }
}
