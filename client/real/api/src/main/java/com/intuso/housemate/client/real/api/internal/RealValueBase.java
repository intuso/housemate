package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.api.internal.object.view.ValueBaseView;

import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public interface RealValueBase<DATA extends ValueBase.Data,
        O,
        TYPE extends RealType<O, ?>,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VIEW extends ValueBaseView,
        VALUE extends RealValueBase<DATA, O, TYPE, LISTENER, VIEW, VALUE>>
        extends ValueBase<DATA,
        List<O>,
        TYPE,
        LISTENER,
        VIEW,
        VALUE> {

    O getValue();

    void setValue(O value);

    /**
     * Sets the object representation of this value
     * @param values the new values
     */
    void setValues(List<O> values);
}
