package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.object.v1_0.api.BaseHousemateObject;

/**
 * Created by tomc on 05/11/15.
 */
public class ObjectMapper {

    private final Function<com.intuso.housemate.object.api.internal.BaseHousemateObject, BaseHousemateObject> toV1_0Function = new Function<com.intuso.housemate.object.api.internal.BaseHousemateObject, BaseHousemateObject>() {
        @Override
        public BaseHousemateObject apply(com.intuso.housemate.object.api.internal.BaseHousemateObject object) {
            return map(object);
        }
    };

    private final Function<BaseHousemateObject, com.intuso.housemate.object.api.internal.BaseHousemateObject> fromV1_0Function = new Function<BaseHousemateObject, com.intuso.housemate.object.api.internal.BaseHousemateObject>() {
        @Override
        public com.intuso.housemate.object.api.internal.BaseHousemateObject apply(BaseHousemateObject object) {
            return map(object);
        }
    };

    private final CommandMapper commandMapper;
    private final DeviceMapper deviceMapper;
    private final ListMapper listMapper;
    private final ParameterMapper parameterMapper;
    private final PropertyMapper propertyMapper;
    private final ValueMapper valueMapper;

    @Inject
    public ObjectMapper(CommandMapper commandMapper, DeviceMapper deviceMapper, ListMapper listMapper, ParameterMapper parameterMapper, PropertyMapper propertyMapper, ValueMapper valueMapper) {
        this.commandMapper = commandMapper;
        this.deviceMapper = deviceMapper;
        this.listMapper = listMapper;
        this.parameterMapper = parameterMapper;
        this.propertyMapper = propertyMapper;
        this.valueMapper = valueMapper;
    }

    public com.intuso.housemate.object.api.internal.BaseHousemateObject<?> map(BaseHousemateObject object) {
        if(object instanceof RealCommand)
            return commandMapper.map((RealCommand)object);
        else if(object instanceof RealDevice)
            return deviceMapper.map((RealDevice<?>)object);
        else if(object instanceof RealList)
            return listMapper.map((RealList<BaseHousemateObject>)object, fromV1_0Function, toV1_0Function);
        else if(object instanceof RealParameter)
            return parameterMapper.map((RealParameter<?>)object);
        else if(object instanceof RealProperty)
            return propertyMapper.map((RealProperty<?>)object);
        else if(object instanceof RealValue)
            return valueMapper.map((RealValue<?>)object);
        return null;
    }

    public BaseHousemateObject map(com.intuso.housemate.object.api.internal.BaseHousemateObject object) {
        if(object instanceof com.intuso.housemate.client.real.api.internal.RealCommand)
            return commandMapper.map((com.intuso.housemate.client.real.api.internal.RealCommand)object);
        else if(object instanceof com.intuso.housemate.client.real.api.internal.RealDevice)
            return deviceMapper.map((com.intuso.housemate.client.real.api.internal.RealDevice<?>)object);
        else if(object instanceof com.intuso.housemate.client.real.api.internal.RealList)
            return listMapper.map((com.intuso.housemate.client.real.api.internal.RealList<com.intuso.housemate.object.api.internal.BaseHousemateObject>)object, toV1_0Function, fromV1_0Function);
        else if(object instanceof com.intuso.housemate.client.real.api.internal.RealParameter)
            return parameterMapper.map((com.intuso.housemate.client.real.api.internal.RealParameter<?>)object);
        else if(object instanceof com.intuso.housemate.client.real.api.internal.RealProperty)
            return propertyMapper.map((com.intuso.housemate.client.real.api.internal.RealProperty<?>)object);
        else if(object instanceof com.intuso.housemate.client.real.api.internal.RealValue)
            return valueMapper.map((com.intuso.housemate.client.real.api.internal.RealValue<?>)object);
        return null;
    }
}
