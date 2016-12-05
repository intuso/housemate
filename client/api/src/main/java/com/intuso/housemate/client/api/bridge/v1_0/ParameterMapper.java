package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Parameter;

/**
 * Created by tomc on 02/12/16.
 */
public class ParameterMapper implements ObjectMapper<Parameter.Data, com.intuso.housemate.client.api.internal.object.Parameter.Data> {

    @Override
    public Parameter.Data map(com.intuso.housemate.client.api.internal.object.Parameter.Data data) {
        if(data == null)
            return null;
        return new Parameter.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Parameter.Data map(Parameter.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Parameter.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }
}
