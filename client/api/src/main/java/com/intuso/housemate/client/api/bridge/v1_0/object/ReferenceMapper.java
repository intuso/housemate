package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Reference;

/**
 * Created by tomc on 02/12/16.
 */
public class ReferenceMapper implements ObjectMapper<Reference.Data, com.intuso.housemate.client.api.internal.object.Reference.Data> {

    @Override
    public Reference.Data map(com.intuso.housemate.client.api.internal.object.Reference.Data data) {
        if(data == null)
            return null;
        return new Reference.Data(data.getId(), data.getName(), data.getDescription(), data.getPath());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Reference.Data map(Reference.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Reference.Data(data.getId(), data.getName(), data.getDescription(), data.getPath());
    }
}
