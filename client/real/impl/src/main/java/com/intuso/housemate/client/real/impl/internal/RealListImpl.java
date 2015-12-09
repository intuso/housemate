package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Iterator;

/**
 */
public final class RealListImpl<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>>
        extends RealObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, List.Listener<? super CHILD>>
        implements RealList<CHILD> {

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the list's id
     * @param name the list's name
     * @param description the list's description
     */
    public RealListImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description) {
        super(logger, listenersFactory, new ListData<CHILD_DATA>(id, name, description));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the list's id
     * @param name the list's name
     * @param description the list's description
     * @param elements the list's elements
     */
    public RealListImpl(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, java.util.List<CHILD> elements) {
        this(logger, listenersFactory, id, name, description);
        for(CHILD element : elements)
            addChild(element);
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super CHILD> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(CHILD element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    public void childObjectAdded(String childName, CHILD child) {
        super.childObjectAdded(childName, child);
        for(Listener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childName, CHILD child) {
        super.childObjectRemoved(childName, child);
        for(Listener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }

    @Override
    public final CHILD get(String name) {
        return getChild(name);
    }

    /**
     * Adds an element to the list
     * @param element the element to add
     */
    public void add(CHILD element) {
        addChild(element);
    }

    /**
     * Removes an elements from the list
     * @param id the id of the element to remove
     * @return the removed element, or null if there was none for the id
     */
    public CHILD remove(String id) {
        CHILD result = removeChild(id);
        if(result != null)
            result.uninit();
        return result;
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getChildren().iterator();
    }

    /**
     * Resends all elements of the list to the server. Used when the server instance has changed and the server needs
     * to be retold of all objects
     */
    public void resendElements() {
        for(CHILD child : this)
            sendMessage(ADD_TYPE, child.getData());
    }
}
