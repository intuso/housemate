package com.intuso.housemate.object.api.internal;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <OBJECT> the type of the list's elements
 */
public interface List<OBJECT> extends BaseHousemateObject<List.Listener<? super OBJECT>>, Iterable<OBJECT> {

    /**
     * Get an element from the list by name
     * @param name the name of the element to get
     * @return the element for that name, or null if there is none
     */
    OBJECT get(String name);

    /**
     * Get the number of elements in the list
     * @return the number of elements in the list
     */
    int size();

    /**
     * Add a list listener to the list object
     * @param listener the listener to add
     * @param callForExistingElements whether the listener should be called for elements already in the list
     * @return the listener registration
     */
    ListenerRegistration addObjectListener(Listener<? super OBJECT> listener, boolean callForExistingElements);

    /**
     *
     * Listener interface for lists
     */
    interface Listener<OBJECT>
            extends ObjectListener {

        /**
         * Notifies that an element was added to the list
         * @param element the element that was added
         */
        void elementAdded(OBJECT element);

        /**
         * Notifies that an element was removed from the list
         * @param element the element that was removed
         */
        void elementRemoved(OBJECT element);
    }
}
