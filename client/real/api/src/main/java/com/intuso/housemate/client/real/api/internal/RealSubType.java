package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.SubType;

/**
 * @param <O> the type of the sub type's value
 */
public interface RealSubType<O>
        extends SubType<RealSubType<O>> {
    RealType<O> getType();
}
