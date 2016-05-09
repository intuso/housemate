package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.SubType;

/**
 * @param <O> the type of the sub type's value
 */
public interface RealSubType<O,
        TYPE extends com.intuso.housemate.client.real.api.internal.RealType<O, ?>,
        SUB_TYPE extends RealSubType<O, TYPE, SUB_TYPE>>
        extends SubType<TYPE, SUB_TYPE> {}
