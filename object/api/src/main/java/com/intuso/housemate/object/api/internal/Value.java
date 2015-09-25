package com.intuso.housemate.object.api.internal;

/**
 * @param <DATA_TYPE> the type of the value's type
 * @param <VALUE> the type of the value
 */
public interface Value<DATA_TYPE, VALUE extends Value<?, ?>>
        extends ValueBase<DATA_TYPE, Value.Listener<? super VALUE>, VALUE> {

    /**
     * Gets the value's type's id
     * @return the value's type's id
     */
    String getTypeId();

    /**
     * Gets the value's value
     * @return the value's value
     */
    DATA_TYPE getValue();

    /**
     *
     * Listener interface for values
     */
    interface Listener<VALUE extends Value<?, ?>> extends ValueBase.Listener<VALUE> {}

    /**
     *
     * Interface to show that the implementing object has a list of values
     */
    interface Container<VALUES extends List<? extends Value<?, ?>>> {

        /**
         * Gets the value list
         * @return the value list
         */
        VALUES getValues();
    }
}
