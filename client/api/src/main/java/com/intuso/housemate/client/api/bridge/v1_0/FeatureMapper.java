package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Feature;

/**
 * Created by tomc on 02/12/16.
 */
public class FeatureMapper implements ObjectMapper<Feature.Data, com.intuso.housemate.client.api.internal.object.Feature.Data> {

    @Override
    public Feature.Data map(com.intuso.housemate.client.api.internal.object.Feature.Data data) {
        if(data == null)
            return null;
        return new Feature.Data(data.getId(), data.getName(), data.getDescription());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Feature.Data map(Feature.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Feature.Data(data.getId(), data.getName(), data.getDescription());
    }
}
