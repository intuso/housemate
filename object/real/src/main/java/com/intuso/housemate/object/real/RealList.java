package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.wrapper.Wrapper;
import com.intuso.utilities.wrapper.WrapperListener;

import java.util.Iterator;

/**
 */
public final class RealList<
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>>
        extends RealObject<ListWrappable<CHILD_DATA>, CHILD_DATA, CHILD, ListListener<? super CHILD>>
        implements List<CHILD>, WrapperListener<CHILD> {

    /**
     * @param resources {@inheritDoc}
     * @param id the list's id
     * @param name the list's name
     * @param description the list's description
     */
    public RealList(RealResources resources, String id, String name, String description) {
        super(resources, new ListWrappable<CHILD_DATA>(id, name, description));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the list's id
     * @param name the list's name
     * @param description the list's description
     * @param elements the list's elements
     */
    public RealList(RealResources resources, String id, String name, String description, java.util.List<CHILD> elements) {
        this(resources, id, name, description);
        for(CHILD element : elements)
            addWrapper(element);
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
        result.add(addWrapperListener(this));
        return result;
    }

    @Override
    public void childWrapperAdded(String childName, CHILD wrapper) {
        wrapper.init(this);
        sendMessage(ADD_TYPE, wrapper.getWrappable());
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    @Override
    public void childWrapperRemoved(String name, CHILD wrapper) {
        wrapper.uninit();
        sendMessage(REMOVE_TYPE, wrapper.getWrappable());
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(wrapper);
    }

    @Override
    public void ancestorAdded(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        // don't need to worry about ancestors other than children, handled above
    }

    @Override
    public void ancestorRemoved(String ancestorPath, Wrapper<?, ?, ?, ?> wrapper) {
        // don't need to worry about ancestors other than children, handled above
    }

    @Override
    public final CHILD get(String name) {
        return getWrapper(name);
    }

    /**
     * Adds an element to the list
     * @param element the element to add
     */
    public void add(CHILD element) {
        addWrapper(element);
    }

    /**
     * Removes an elements from the list
     * @param id the id of the element to remove
     * @return the removed element, or null if there was none for the id
     */
    public CHILD remove(String id) {
        CHILD result = removeWrapper(id);
        if(result != null)
            result.uninit();
        return result;
    }

    @Override
    public int size() {
        return getWrappers().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getWrappers().iterator();
    }

    /**
     * Resends all elements of the list to the broker. Used when the broker instance has changed and the broker needs
     * to be retold of all objects
     */
    public void resendElements() {
        for(CHILD subWrapper : this)
            sendMessage(ADD_TYPE, subWrapper.getWrappable());
    }
}
