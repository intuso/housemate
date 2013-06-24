package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

/**
 */
public abstract class RealSimpleType<O> extends RealType<SimpleTypeWrappable, NoChildrenWrappable, O> {

    private final TypeSerialiser<O> serialiser;

    protected RealSimpleType(RealResources resources, SimpleTypeWrappable.Type type, TypeSerialiser<O> serialiser) {
        super(resources, new SimpleTypeWrappable(type));
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
