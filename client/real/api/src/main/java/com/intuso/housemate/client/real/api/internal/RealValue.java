package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.view.ValueView;

import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public interface RealValue<O,
        TYPE extends RealType<O, ?>,
        VALUE extends RealValue<O, TYPE, VALUE>>
        extends RealValueBase<Value.Data, O, TYPE, Value.Listener<? super VALUE>, ValueView, VALUE>,
        Value<List<O>,
        TYPE,
        VALUE> {}
