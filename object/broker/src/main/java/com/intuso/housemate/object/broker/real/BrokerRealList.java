package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObject;
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
public final class BrokerRealList<SWBL extends HousemateObjectWrappable<?>,
            SWR extends BrokerRealObject<? extends SWBL, ?, ?, ?>>
        extends BrokerRealObject<ListWrappable<SWBL>, SWBL, SWR, ListListener<? super SWR>>
        implements List<SWR>, WrapperListener<SWR> {

    private ListenerRegistration listenerRegistration;

    public BrokerRealList(BrokerRealResources resources, String id, String name, String description) {
        super(resources, new ListWrappable(id, name, description));
    }

    public BrokerRealList(BrokerRealResources resources, String id, String name, String description, java.util.List<SWR> elements) {
        this(resources, id, name, description);
        for(SWR element : elements)
            addWrapper(element);
    }

    @Override
    public ListenerRegistration addObjectListener(ListListener<? super SWR> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(HousemateObject<?, ?, ?, ?, ?> element : this)
                listener.elementAdded((SWR)element);
        return listenerRegistration;
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addWrapperListener(this));
        return result;
    }

    @Override
    public void childWrapperAdded(String childName, SWR wrapper) {
        wrapper.init(this);
        for(ListListener<? super SWR> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    @Override
    public void childWrapperRemoved(String name, SWR wrapper) {
        wrapper.uninit();
        for(ListListener<? super SWR> listener : getObjectListeners())
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
    public final SWR get(String name) {
        return (SWR)getWrapper(name);
    }

    public void add(SWR element) {
        addWrapper(element);
    }

    public SWR remove(String name) {
        return (SWR) removeWrapper(name);
    }

    @Override
    public int size() {
        return getWrappers().size();
    }

    @Override
    public Iterator<SWR> iterator() {
        return getWrappers().iterator();
    }
}
