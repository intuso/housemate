package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.Parameter;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O>
        extends Parameter<RealParameter<O>> {
    com.intuso.housemate.client.real.api.internal.RealType<O> getType();
}
