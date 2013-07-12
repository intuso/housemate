package com.intuso.housemate.api.object;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Base interface for all other object interfaces
 * @param <L> the type of the object's listeners
 */
public interface BaseHousemateObject<L extends ObjectListener> {

    /**
     * Gets the object's id
     * @return the object's id
     */
    public String getId();

    /**
     * Gets the object's name
     * @return the object's name
     */
    public String getName();

    /**
     * Gets the object's description
     * @return the object's id description
     */
    public String getDescription();

    /**
     * Gets the object's path
     * @return the object's path
     */
    public String[] getPath();

    /**
     * Adds a listener to this object
     * @param listener the listener to add
     * @return the listener registration
     */
    public ListenerRegistration addObjectListener(L listener);
}
