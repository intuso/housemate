package com.intuso.housemate.client.api.internal.object;

import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <ELEMENT> the type of the list's elements
 */
public interface List<ELEMENT,
        LIST extends List<ELEMENT, ?>>
        extends Object<List.Listener<? super ELEMENT, ? super LIST>>,
        Iterable<ELEMENT> {

    /**
     * Get an element from the list by id
     * @param id the id of the element to get
     * @return the element with the given id, or null if there is none
     */
    ELEMENT get(String id);

    /**
     * Get an element from the list by name. Many elements could have the same name. This will return the first one. For
     * other behaviour, you should iterate the list
     * @param name the name of the element to get
     * @return the first element with the given name, or null if there is none
     */
    ELEMENT getByName(String name);

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
    ListenerRegistration addObjectListener(Listener<? super ELEMENT, ? super LIST> listener, boolean callForExistingElements);

    /**
     *
     * Listener interface for lists
     */
    interface Listener<OBJECT, LIST extends List<OBJECT, ?>>
            extends Object.Listener {

        /**
         * Notifies that an element was added to the list
         * @param element the element that was added
         */
        void elementAdded(LIST list, OBJECT element);

        /**
         * Notifies that an element was removed from the list
         * @param element the element that was removed
         */
        void elementRemoved(LIST list, OBJECT element);
    }

    /**
     * Data object for a list
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "list";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }
}
