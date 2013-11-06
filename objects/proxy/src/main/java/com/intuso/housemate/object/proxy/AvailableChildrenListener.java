package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.utilities.listener.Listener;

/**
 * Listener for when child objects become (un)available
 * @param <OBJECT>
 */
public interface AvailableChildrenListener<OBJECT extends ProxyObject<?, ?, ?, ?, ?, ?, ?>> extends Listener {

    /**
     * Callback for when a child object was added
     * @param object the object that has a new child
     * @param childOverview the overview of the child's data
     */
    public void childAdded(OBJECT object, ChildOverview childOverview);

    /**
     * Callback for when a child object was removed
     * @param object the object that has a child removed
     * @param childOverview the overview of the child's data
     */
    public void childRemoved(OBJECT object, ChildOverview childOverview);
}

