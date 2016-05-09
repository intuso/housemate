package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Value;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O,
        TYPE extends com.intuso.housemate.client.real.api.internal.RealType<O, ?>,
        VALUE extends RealValue<O, TYPE, VALUE>>
        extends com.intuso.housemate.client.real.api.internal.RealValueBase<O, TYPE, Value.Listener<? super VALUE>, VALUE>,
        Value<O,
        TYPE,
        VALUE> {}
