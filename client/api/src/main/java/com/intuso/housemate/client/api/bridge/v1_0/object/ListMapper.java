package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.List;

/**
 * Created by tomc on 02/12/16.
 */
public class ListMapper implements ObjectMapper<List.Data, com.intuso.housemate.client.api.internal.object.List.Data> {

    @Override
    public List.Data map(com.intuso.housemate.client.api.internal.object.List.Data data) {
        if(data == null)
            return null;
        return new List.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.List.Data map(List.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.List.Data(data.getId(), data.getName(), data.getDescription());
    }
}
