package com.intuso.housemate.client.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Property;

/**
 * Created by tomc on 02/12/16.
 */
public class PropertyMapper implements ObjectMapper<Property.Data, com.intuso.housemate.client.api.internal.object.Property.Data> {

    @Override
    public Property.Data map(com.intuso.housemate.client.api.internal.object.Property.Data data) {
        if(data == null)
            return null;
        return new Property.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Property.Data map(Property.Data data) {
        if(data == null)
            return null;
        return new com.intuso.housemate.client.api.internal.object.Property.Data(data.getId(), data.getName(), data.getDescription(), data.getTypePath(), data.getMinValues(), data.getMaxValues());
    }
}
