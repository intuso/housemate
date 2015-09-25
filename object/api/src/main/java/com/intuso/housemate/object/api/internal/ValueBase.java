package com.intuso.housemate.object.api.internal;

/**
 * @param <DATA_TYPE> the type of the value's type
 * @param <VALUE> the type of the value
 */
public interface ValueBase<DATA_TYPE,
        LISTENER extends ValueBase.Listener<? super VALUE>, VALUE extends ValueBase<?, ?, ?>>
        extends BaseHousemateObject<LISTENER> {

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
    interface Listener<VALUE extends ValueBase<?, ?, ?>> extends ObjectListener {

        /**
         * Notifies that the value of this value object is about to be changed
         * @param value the value object whose value is about to be changed
         */
        void valueChanging(VALUE value);

        /**
         * Notifies that the value of this value object has been changed
         * @param value the value object whose value has just been changed
         */
        void valueChanged(VALUE value);
    }
}
