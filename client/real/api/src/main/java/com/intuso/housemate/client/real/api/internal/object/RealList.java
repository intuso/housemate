package com.intuso.housemate.client.real.api.internal.object;

import com.intuso.housemate.client.api.internal.object.List;

/**
 */
public interface RealList<ELEMENT, LIST extends RealList<ELEMENT, ?>>
        extends List<ELEMENT, LIST> {

    /**
     * Adds an element to the list
     * @param element the element to add
     */
    void add(ELEMENT element);

    /**
     * Removes an elements from the list
     * @param id the id of the element to remove
     * @return the removed element, or null if there was none for the id
     */
    ELEMENT remove(String id);
}
