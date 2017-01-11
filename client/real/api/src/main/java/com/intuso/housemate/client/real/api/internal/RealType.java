package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.type.TypeSerialiser;

/**
 * @param <O> the type of the type instances
 */
public interface RealType<O, TYPE extends RealType<O, TYPE>>
        extends Type<TYPE>, TypeSerialiser<O> {

    interface Container<TYPE extends RealType<?, ?>, TYPES extends RealList<? extends TYPE, ?>> extends Type.Container<TYPES> {
        void addType(TYPE type);
        void removeType(TYPE type);
    }
}
