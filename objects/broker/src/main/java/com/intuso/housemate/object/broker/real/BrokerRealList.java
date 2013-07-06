package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.wrapper.Wrapper;
import com.intuso.utilities.wrapper.WrapperListener;

import java.util.Iterator;

/**
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 */
public final class BrokerRealList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends BrokerRealObject<? extends CHILD_DATA, ?, ?, ?>>
        extends BrokerRealObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, ListListener<? super CHILD>>
        implements List<CHILD>, WrapperListener<CHILD> {

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     */
    public BrokerRealList(BrokerRealResources resources, String id, String name, String description) {
        super(resources, new ListData<CHILD_DATA>(id, name, description));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param elements the initial elements in the list
     */
    public BrokerRealList(BrokerRealResources resources, String id, String name, String description, java.util.List<CHILD> elements) {
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
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    @Override
    public void childWrapperRemoved(String name, CHILD wrapper) {
        wrapper.uninit();
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
     * Removes an element from the list
     * @param name the name of the element to remove
     * @return the element that was removed, or null if it didn't exist
     */
    public CHILD remove(String name) {
        return removeWrapper(name);
    }

    @Override
    public int size() {
        return getWrappers().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getWrappers().iterator();
    }
}
