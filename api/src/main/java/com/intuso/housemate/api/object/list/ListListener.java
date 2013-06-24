package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for lists
 */
public interface ListListener<WR>
        extends ObjectListener {

    /**
     * Notifies that an element was added to the list
     * @param element the element that was added
     */
    public void elementAdded(WR element);

    /**
     * Notifies that an element was removed from the list
     * @param element the element that was removed
     */
    public void elementRemoved(WR element);
}
