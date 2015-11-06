package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.Type;
import com.intuso.housemate.object.api.internal.TypeSerialiser;

/**
 * @param <O> the type of the type instances
 */
public interface RealType<O>
        extends Type<RealType<O>>, TypeSerialiser<O> {

    interface Container extends Type.Container<RealList<RealType<?>>> {
        void addType(RealType<?> type);
        void removeType(RealType<?> type);
    }
}
