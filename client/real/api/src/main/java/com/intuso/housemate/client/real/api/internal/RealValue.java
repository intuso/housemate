package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O>
        extends com.intuso.housemate.client.real.api.internal.RealValueBase<O, Value.Listener<? super RealValue<O>>, RealValue<O>>,
        Value<TypeInstances, RealValue<O>> {}
