package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.List;

/**
 */
public interface RealList<CHILD>
        extends List<CHILD> {

    /**
     * Adds an element to the list
     * @param element the element to add
     */
    void add(CHILD element);

    /**
     * Removes an elements from the list
     * @param id the id of the element to remove
     * @return the removed element, or null if there was none for the id
     */
    CHILD remove(String id);

    /**
     * Resends all elements of the list to the server. Used when the server instance has changed and the server needs
     * to be retold of all objects
     */
    void resendElements();
}
