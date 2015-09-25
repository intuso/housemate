package com.intuso.housemate.object.api.internal;

/**
 * @param <ROOT> the type of the root
 */
public interface Root<LISTENER extends Root.Listener<? super ROOT>,
        ROOT extends Root<?, ?>> extends BaseHousemateObject<LISTENER> {

    /**
     *
     * Listener interface for root objects
     */
    interface Listener<ROOT extends Root<?, ?>> extends ObjectListener {}
}
