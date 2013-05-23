package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.BaseObject;
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
 * Date: 17/07/12
 * Time: 23:42
 * To change this template use File | Settings | File Templates.
 */
public class ListBridge<
            OWR extends BaseObject<?>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>>
        extends BridgeObject<ListWrappable<SWBL>, SWBL, SWR, ListBridge<OWR, SWBL, SWR>, ListListener<? super SWR>>
        implements List<SWR>, WrapperListener<SWR> {

    private ListenerRegistration<?> otherListListener;

    public ListBridge(BrokerBridgeResources resources, List<? extends OWR> list, final Function<? super OWR, ? extends SWR> converter) {
        super(resources, new ListWrappable(list.getId(), list.getName(), list.getDescription()));
        otherListListener = list.addObjectListener(new ListListener<OWR>() {
            @Override
            public void elementAdded(OWR element) {
                addWrapper(converter.apply(element));
            }

            @Override
            public void elementRemoved(OWR element) {
                removeWrapper(element.getId());
            }
        }, true);
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
        if(otherListListener != null)
            result.add(otherListListener);
        result.add(addWrapperListener(this));
        return result;
    }

    @Override
    public void childWrapperAdded(String childName, SWR wrapper) {
        wrapper.init(this);
        addLoadedBy(wrapper);
        broadcastMessage(ADD, wrapper.getWrappable().deepClone());
        for(ListListener<? super SWR> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    @Override
    public void childWrapperRemoved(String name, SWR wrapper) {
        wrapper.uninit();
        broadcastMessage(REMOVE, wrapper.getWrappable());
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
    public SWR get(String name) {
        return getWrapper(name);
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
