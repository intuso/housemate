package com.intuso.housemate.object.api.internal;

/**
 * Created by tomc on 16/09/15.
 */
public interface Failable<ERROR_VALUE extends Value<?, ?>> {

    ERROR_VALUE getErrorValue();

    /**
     * Listener interface for automations
     */
    interface Listener<FAILABLE extends Failable<?>> {

        /**
         * Notifies that the primary object's error has changed
         * @param failable the object that is in error (or not)
         * @param error the description of the error
         */
        void error(FAILABLE failable, String error);
    }
}
