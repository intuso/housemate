package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.listeners.ListenerRegistration;
import com.intuso.wrapper.Wrapper;
import com.intuso.wrapper.WrapperListener;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/07/12
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public final class RealList<SWBL extends HousemateObjectWrappable<?>,
            SWR extends RealObject<? extends SWBL, ?, ?, ?>>
        extends RealObject<ListWrappable<SWBL>, SWBL, SWR, ListListener<? super SWR>>
        implements List<SWR>, WrapperListener<SWR> {

    public RealList(RealResources resources, String id, String name, String description) {
        super(resources, new ListWrappable<SWBL>(id, name, description));
    }

    public RealList(RealResources resources, String id, String name, String description, java.util.List<SWR> elements) {
        this(resources, id, name, description);
        for(SWR element : elements)
            addWrapper(element);
    }

    @Override
    public ListenerRegistration<? super ListListener<? super SWR>> addObjectListener(ListListener<? super SWR> listener, boolean callForExistingElements) {
        ListenerRegistration<? super ListListener<? super SWR>> listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(SWR element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    protected java.util.List<ListenerRegistration<?>> registerListeners() {
        java.util.List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addWrapperListener(this));
        return result;
    }

    @Override
    public void childWrapperAdded(String childName, SWR wrapper) {
        wrapper.init(this);
        sendMessage(ADD, wrapper.getWrappable());
        for(ListListener<? super SWR> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    @Override
    public void childWrapperRemoved(String name, SWR wrapper) {
        wrapper.uninit();
        sendMessage(REMOVE, wrapper.getWrappable());
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
        return getWrapper(name);
    }

    public void add(SWR element) {
        addWrapper(element);
    }

    public SWR remove(String name) {
        SWR result = removeWrapper(name);
        if(result != null)
            result.uninit();
        return result;
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
