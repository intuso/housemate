package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealType<T extends TypeWrappable<SWBL>, SWBL extends HousemateObjectWrappable<?>, O>
        extends RealObject<T, SWBL, RealObject<SWBL, ?, ?, ?>, TypeListener>
        implements Type, TypeSerialiser<O> {

    private TypeSerialiser<O> serialiser;

    protected RealType(RealResources resources, T wrappable, TypeSerialiser<O> serialiser) {
        super(resources, wrappable);
        this.serialiser = serialiser;
    }

    @Override
    public final String serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    public final O deserialise(String value) {
        return serialiser.deserialise(value);
    }
}
