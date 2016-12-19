package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Option;

/**
 * Created by tomc on 02/12/16.
 */
public class OptionMapper implements ObjectMapper<Option.Data, com.intuso.housemate.client.api.internal.object.Option.Data> {

    @Override
    public Option.Data map(com.intuso.housemate.client.api.internal.object.Option.Data data) {
        if(data == null)
            return null;
        return new Option.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Option.Data map(Option.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Option.Data(data.getId(), data.getName(), data.getDescription());
    }
}
