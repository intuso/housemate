package com.intuso.housemate.client.api.internal.type;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * Reference for an object containing the object's path, and the object if it exists
 * @param <O>
 */
public interface ObjectReference<O extends Object<?, ?, ?>> {

    /**
     * Gets the path
     * @return the path
     */
    String[] getPath();

    /**
     * Gets the object
     * @return the object
     */
    O getObject();

    ManagedCollection.Registration addListener(Listener<O> listener);

    interface Listener<O extends Object<?, ?, ?>> {
        void available(O object);
        void unavailable();
    }
}
