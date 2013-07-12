package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.wrapper.Wrapper;

import java.util.Iterator;

/**
 */
public class ListBridge<
            WBL extends HousemateData<?>,
            OWR extends BaseObject<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends BridgeObject<ListData<WBL>, WBL, WR, ListBridge<WBL, OWR, WR>, ListListener<? super WR>>
        implements List<WR> {

    private ListenerRegistration otherListListener;

    public ListBridge(BrokerBridgeResources resources, List<? extends OWR> list, final Function<? super OWR, ? extends WR> converter) {
        super(resources, new ListData(list.getId(), list.getName(), list.getDescription()));
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
        result.add(otherListListener);
        return result;
    }

    @Override
    public void childWrapperAdded(String childId, WR child) {
        super.childWrapperAdded(childId, child);
        child.init(this);
        addLoadedBy(child);
        broadcastMessage(ADD_TYPE, child.getData().deepClone());
        for(ListListener<? super WR> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childWrapperRemoved(String childId, WR child) {
        super.childWrapperRemoved(childId, child);
        child.uninit();
        broadcastMessage(REMOVE_TYPE, child.getData());
        for(ListListener<? super WR> listener : getObjectListeners())
            listener.elementRemoved(child);
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
