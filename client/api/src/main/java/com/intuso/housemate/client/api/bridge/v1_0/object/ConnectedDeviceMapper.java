package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.ConnectedDevice;

/**
 * Created by tomc on 02/12/16.
 */
public class ConnectedDeviceMapper implements ObjectMapper<ConnectedDevice.Data, com.intuso.housemate.client.api.internal.object.ConnectedDevice.Data> {

    @Override
    public ConnectedDevice.Data map(com.intuso.housemate.client.api.internal.object.ConnectedDevice.Data data) {
        if(data == null)
            return null;
        return new ConnectedDevice.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.ConnectedDevice.Data map(ConnectedDevice.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.ConnectedDevice.Data(data.getId(), data.getName(), data.getDescription());
    }
}
