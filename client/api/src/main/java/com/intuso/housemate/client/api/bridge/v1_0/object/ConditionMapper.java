package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Condition;

/**
 * Created by tomc on 02/12/16.
 */
public class ConditionMapper implements ObjectMapper<Condition.Data, com.intuso.housemate.client.api.internal.object.Condition.Data> {

    @Override
    public Condition.Data map(com.intuso.housemate.client.api.internal.object.Condition.Data data) {
        if(data == null)
            return null;
        return new Condition.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Condition.Data map(Condition.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Condition.Data(data.getId(), data.getName(), data.getDescription());
    }
}
