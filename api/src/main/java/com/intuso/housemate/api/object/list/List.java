package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <T> the type of the list's elements
 */
public interface List<T> extends BaseHousemateObject<ListListener<? super T>>, Iterable<T> {

    /**
     * Get an element from the list by name
     * @param name the name of the element to get
     * @return the element for that name, or null if there is none
     */
    public T get(String name);

    /**
     * Get the number of elements in the list
     * @return the number of elements in the list
     */
    public int size();

    /**
     * Add a list listener to the list object
     * @param listener the listener to add
     * @param callForExistingElements whether the listener should be called for elements already in the list
     * @return the listener registration
     */
    public ListenerRegistration addObjectListener(ListListener<? super T> listener, boolean callForExistingElements);
}
