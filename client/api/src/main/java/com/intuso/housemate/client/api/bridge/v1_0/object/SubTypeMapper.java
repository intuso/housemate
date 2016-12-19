package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.SubType;

/**
 * Created by tomc on 02/12/16.
 */
public class SubTypeMapper implements ObjectMapper<SubType.Data, com.intuso.housemate.client.api.internal.object.SubType.Data> {

    @Override
    public SubType.Data map(com.intuso.housemate.client.api.internal.object.SubType.Data data) {
        if(data == null)
            return null;
        return new SubType.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.SubType.Data map(SubType.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.SubType.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }
}
