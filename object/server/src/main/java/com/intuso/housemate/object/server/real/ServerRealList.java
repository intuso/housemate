package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;

import java.util.Iterator;

/**
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 */
public final class ServerRealList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ServerRealObject<? extends CHILD_DATA, ?, ?, ?>>
        extends ServerRealObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, ListListener<? super CHILD>>
        implements List<CHILD>, ObjectListener<CHILD> {

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public ServerRealList(Log log, ListenersFactory listenersFactory, String id, String name, String description) {
        super(log, listenersFactory, new ListData<CHILD_DATA>(id, name, description));
    }

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param elements the initial elements in the list
     */
    public ServerRealList(Log log, ListenersFactory listenersFactory, String id, String name, String description, java.util.List<CHILD> elements) {
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
    public void childObjectAdded(String id, CHILD child) {
        child.init(this);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String id, CHILD child) {
        child.uninit();
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // don't need to worry about ancestors other than children, handled above
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // don't need to worry about ancestors other than children, handled above
    }

    @Override
    public final CHILD get(String id) {
        return getChild(id);
    }

    /**
     * Adds an element to the list
     * @param element the element to add
     */
    public void add(CHILD element) {
        addChild(element);
    }

    /**
     * Removes an element from the list
     * @param id the name of the element to remove
     * @return the element that was removed, or null if it didn't exist
     */
    public CHILD remove(String id) {
        return removeChild(id);
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getChildren().iterator();
    }
}
