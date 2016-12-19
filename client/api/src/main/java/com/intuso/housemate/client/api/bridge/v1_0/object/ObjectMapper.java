package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Object;

/**
 * Created by tomc on 02/12/16.
 */
public interface ObjectMapper<VERSION_DATA extends Object.Data, INTERNAL_DATA extends com.intuso.housemate.client.api.internal.object.Object.Data> {
    INTERNAL_DATA map(VERSION_DATA data);
    VERSION_DATA map(INTERNAL_DATA data);
}
