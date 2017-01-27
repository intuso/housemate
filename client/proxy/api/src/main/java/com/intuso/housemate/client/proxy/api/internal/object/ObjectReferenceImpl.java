package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

/**
 * Reference for an object containing the object's path, and the object if it exists
 * @param <O>
 */
public class ObjectReferenceImpl<O extends Object<?>> implements ObjectReference<O> {

    private final ManagedCollection<Listener<O>> listeners;

    private final String[] path;
    private O object;

    /**
     * @param path the path to the object
     */
    public ObjectReferenceImpl(ManagedCollectionFactory managedCollectionFactory, String[] path) {
        this(managedCollectionFactory, path, null);
    }

    /**
     * @param path the object's path
     * @param object the object
     */
    public ObjectReferenceImpl(ManagedCollectionFactory managedCollectionFactory, String[] path, O object) {
        this.listeners = managedCollectionFactory.create();
        this.path = path;
        this.object = object;
    }

    /**
     * Gets the path
     * @return the path
     */
    public String[] getPath() {
        return path;
    }

    /**
     * Gets the object
     * @return the object
     */
    public O getObject() {
        return object;
    }

    /**
     * Sets the object
     * @param object the object
     */
    public void setObject(O object) {
        this.object = object;
        if(object != null)
            for(Listener<O> listener : listeners)
                listener.available(object);
        else
            for(Listener<O> listener : listeners)
                listener.unavailable();
    }

    @Override
    public ManagedCollection.Registration addListener(Listener<O> listener) {
        return listeners.add(listener);
    }
}
