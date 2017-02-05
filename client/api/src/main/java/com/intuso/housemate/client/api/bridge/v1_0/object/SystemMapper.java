package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.System;

/**
 * Created by tomc on 02/12/16.
 */
public class SystemMapper implements ObjectMapper<System.Data, com.intuso.housemate.client.api.internal.object.System.Data> {

    @Override
    public System.Data map(com.intuso.housemate.client.api.internal.object.System.Data data) {
        if(data == null)
            return null;
        return new System.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.System.Data map(System.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.System.Data(data.getId(), data.getName(), data.getDescription());
    }
}
