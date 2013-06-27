package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.BaseObject;
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
public class ListBridge<
            WBL extends HousemateObjectWrappable<?>,
            OWR extends BaseObject<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends BridgeObject<ListWrappable<WBL>, WBL, WR, ListBridge<WBL, OWR, WR>, ListListener<? super WR>>
        implements List<WR>, WrapperListener<WR> {

    private ListenerRegistration otherListListener;

    public ListBridge(BrokerBridgeResources resources, List<? extends OWR> list, final Function<? super OWR, ? extends WR> converter) {
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
    public ListenerRegistration addObjectListener(ListListener<? super WR> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(WR element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        if(otherListListener != null)
            result.add(otherListListener);
        result.add(addWrapperListener(this));
        return result;
    }

    @Override
    public void childWrapperAdded(String childName, WR wrapper) {
        wrapper.init(this);
        addLoadedBy(wrapper);
        broadcastMessage(ADD_TYPE, wrapper.getData().deepClone());
        for(ListListener<? super WR> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    @Override
    public void childWrapperRemoved(String name, WR wrapper) {
        wrapper.uninit();
        broadcastMessage(REMOVE_TYPE, wrapper.getData());
        for(ListListener<? super WR> listener : getObjectListeners())
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
    public WR get(String name) {
        return getWrapper(name);
    }

    @Override
    public int size() {
        return getWrappers().size();
    }

    @Override
    public Iterator<WR> iterator() {
        return getWrappers().iterator();
    }
}
