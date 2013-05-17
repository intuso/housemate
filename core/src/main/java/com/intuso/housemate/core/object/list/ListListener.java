package com.intuso.housemate.core.object.list;

import com.intuso.housemate.core.object.ObjectListener;

/**
 * Interface to implement to receive updates about new/removed elements in a list
 * @author tclabon
 */
public interface ListListener<WR>
        extends ObjectListener {
    public void elementAdded(WR element);
    public void elementRemoved(WR element);
}
