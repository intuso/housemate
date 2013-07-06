package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

/**
 * Base class for types that have a simple type, such as string, integer etc
 */
public abstract class RealSimpleType<O> extends RealType<SimpleTypeData, NoChildrenData, O> {

    private final TypeSerialiser<O> serialiser;

    /**
     * @param resources the resources
     * @param type the type of the simple type
     * @param serialiser the serialiser for the type
     */
    protected RealSimpleType(RealResources resources, SimpleTypeData.Type type, TypeSerialiser<O> serialiser) {
        super(resources, new SimpleTypeData(type));
        this.serialiser = serialiser;
    }

    @Override
    public TypeInstance serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    public O deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }
}
