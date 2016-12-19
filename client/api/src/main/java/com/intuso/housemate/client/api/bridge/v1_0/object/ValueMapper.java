package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Value;

/**
 * Created by tomc on 02/12/16.
 */
public class ValueMapper implements ObjectMapper<Value.Data, com.intuso.housemate.client.api.internal.object.Value.Data> {

    @Override
    public Value.Data map(com.intuso.housemate.client.api.internal.object.Value.Data data) {
        if(data == null)
            return null;
        return new Value.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Value.Data map(Value.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Value.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }
}
