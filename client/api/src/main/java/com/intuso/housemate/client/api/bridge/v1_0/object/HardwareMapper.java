package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Hardware;

/**
 * Created by tomc on 02/12/16.
 */
public class HardwareMapper implements ObjectMapper<Hardware.Data, com.intuso.housemate.client.api.internal.object.Hardware.Data> {

    @Override
    public Hardware.Data map(com.intuso.housemate.client.api.internal.object.Hardware.Data data) {
        if(data == null)
            return null;
        return new Hardware.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Hardware.Data map(Hardware.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Hardware.Data(data.getId(), data.getName(), data.getDescription());
    }
}
