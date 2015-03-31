package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Iterator;

/**
 */
public final class RealList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>>
        extends RealObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, ListListener<? super CHILD>>
        implements List<CHILD> {

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the list's id
     * @param name the list's name
     * @param description the list's description
     */
    public RealList(Log log, ListenersFactory listenersFactory, String id, String name, String description) {
        super(log, listenersFactory, new ListData<CHILD_DATA>(id, name, description));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the list's id
     * @param name the list's name
     * @param description the list's description
     * @param elements the list's elements
     */
    public RealList(Log log, ListenersFactory listenersFactory, String id, String name, String description, java.util.List<CHILD> elements) {
        this(log, listenersFactory, id, name, description);
        for(CHILD element : elements)
            addChild(element);
    }

    @Override
    public ListenerRegistration addObjectListener(ListListener<? super CHILD> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(CHILD element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addChildListener(this));
        return result;
    }

    @Override
    public void childObjectAdded(String childName, CHILD child) {
        super.childObjectAdded(childName, child);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childName, CHILD child) {
        super.childObjectRemoved(childName, child);
        for(ListListener<? super CHILD> listener : getObjectListeners())
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
