package com.intuso.housemate.api.object;

import com.intuso.utilities.listener.Listener;

/**
 *
 * Listener for creation and removal of objects at a certain path
 */
public interface ObjectLifecycleListener extends Listener {

    /**
     * Notifies that a new object was created
     * @param path the path of the new object
     * @param object the object
     */
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?> object);

    /**
     * Notifies that an object has been removed
     * @param path the path of the removed object
     * @param object the object
     */
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?> object);
}
