package com.intuso.housemate.client.api.internal.type;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.utilities.listener.ManagedCollection;
import com.intuso.utilities.listener.ManagedCollectionFactory;

/**
 * Reference for an object containing the object's path, and the object if it exists
 * @param <O>
 */
public class ObjectReference<O extends Object<?>> {

    private final ManagedCollection<Listener<O>> listeners;

    private final String[] path;
    private O object;

    /**
     * @param path the path to the object
     */
    public ObjectReference(ManagedCollectionFactory managedCollectionFactory, String[] path) {
        this(managedCollectionFactory, path, null);
    }

    /**
     * @param path the object's path
     * @param object the object
     */
    public ObjectReference(ManagedCollectionFactory listenersFactory, String[] path, O object) {
        this.listeners = listenersFactory.create();
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
    protected void setObject(O object) {
        this.object = object;
        if(object != null)
            for(Listener<O> listener : listeners)
                listener.available(object);
        else
            for(Listener<O> listener : listeners)
                listener.unavailable();
    }

    public ManagedCollection.Registration addListener(Listener<O> listener) {
        return listeners.add(listener);
    }

    public interface Listener<O extends Object<?>> {
        void available(O object);
        void unavailable();
    }
}
