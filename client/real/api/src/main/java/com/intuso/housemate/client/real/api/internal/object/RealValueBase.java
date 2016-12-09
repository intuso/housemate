package com.intuso.housemate.client.real.api.internal.object;

import com.intuso.housemate.client.api.internal.object.ValueBase;

import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public interface RealValueBase<O,
        TYPE extends RealType<O, ?>,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VALUE extends RealValueBase<O, TYPE, LISTENER, VALUE>>
        extends ValueBase<O,
        TYPE,
        LISTENER,
        VALUE> {

    /**
     * Gets the object representation of this value
     * @return
     */
    Iterable<O> getValues();

    void setValue(O value);

    /**
     * Sets the object representation of this value
     * @param values the new values
     */
    void setValues(List<O> values);
}
