package com.intuso.housemate.client.real.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Parameter;

/**
 * @param <O> the type of the parameter's value
 */
public interface RealParameter<O,
        TYPE extends RealType<O, ?>,
        PARAMETER extends RealParameter<O, TYPE, PARAMETER>>
        extends Parameter<TYPE, PARAMETER> {}
