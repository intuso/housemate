package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.intuso.housemate.client.v1_0.api.object.Type;

/**
 * Created by tomc on 02/12/16.
 */
public class TypeMapper implements ObjectMapper<Type.Data, com.intuso.housemate.client.api.internal.object.Type.Data> {

    @Override
    public Type.Data map(com.intuso.housemate.client.api.internal.object.Type.Data data) {
        if(data == null)
            return null;
        else if(data instanceof com.intuso.housemate.client.api.internal.object.Type.ChoiceData)
            return new Type.ChoiceData(data.getId(), data.getName(), data.getDescription());
        else if(data instanceof com.intuso.housemate.client.api.internal.object.Type.CompositeData)
            return new Type.CompositeData(data.getId(), data.getName(), data.getDescription());
        else if(data instanceof com.intuso.housemate.client.api.internal.object.Type.ObjectData)
            return new Type.ObjectData(data.getId(), data.getName(), data.getDescription());
        else if(data instanceof com.intuso.housemate.client.api.internal.object.Type.RegexData)
            return new Type.RegexData(data.getId(), data.getName(), data.getDescription(), ((com.intuso.housemate.client.api.internal.object.Type.RegexData) data).getRegexPattern());
        else if(data instanceof com.intuso.housemate.client.api.internal.object.Type.PrimitiveData)
            return new Type.PrimitiveData(data.getId(), data.getName(), data.getDescription());
        else
            return null;
    }

    @Override
    public com.intuso.housemate.client.api.internal.object.Type.Data map(Type.Data data) {
        if(data == null)
            return null;
        else if(data instanceof Type.ChoiceData)
            return new com.intuso.housemate.client.api.internal.object.Type.ChoiceData(data.getId(), data.getName(), data.getDescription());
        else if(data instanceof Type.CompositeData)
            return new com.intuso.housemate.client.api.internal.object.Type.CompositeData(data.getId(), data.getName(), data.getDescription());
        else if(data instanceof Type.ObjectData)
            return new com.intuso.housemate.client.api.internal.object.Type.ObjectData(data.getId(), data.getName(), data.getDescription());
        else if(data instanceof Type.RegexData)
            return new com.intuso.housemate.client.api.internal.object.Type.RegexData(data.getId(), data.getName(), data.getDescription(), ((Type.RegexData) data).getRegexPattern());
        else if(data instanceof Type.PrimitiveData)
            return new com.intuso.housemate.client.api.internal.object.Type.PrimitiveData(data.getId(), data.getName(), data.getDescription());
        else
            return null;
    }
}
