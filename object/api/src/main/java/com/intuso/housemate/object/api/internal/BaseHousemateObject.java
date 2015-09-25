package com.intuso.housemate.object.api.internal;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Base interface for all other object interfaces
 * @param <LISTENER> the type of the object's listeners
 */
public interface BaseHousemateObject<LISTENER extends ObjectListener> {

    /**
     * Gets the object's id
     * @return the object's id
     */
    String getId();

    /**
     * Gets the object's name
     * @return the object's name
     */
    String getName();

    /**
     * Gets the object's description
     * @return the object's description
     */
    String getDescription();

    /**
     * Gets the object's path
     * @return the object's path
     */
    String[] getPath();

    /**
     * Adds a listener to this object
     * @param listener the listener to add
     * @return the listener registration
     */
    ListenerRegistration addObjectListener(LISTENER listener);
}
