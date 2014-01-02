package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.object.BaseObject;

import java.util.Iterator;

/**
 */
public class ListBridge<
            WBL extends HousemateData<?>,
            OWR extends BaseHousemateObject<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends BridgeObject<ListData<WBL>, WBL, WR, ListBridge<WBL, OWR, WR>, ListListener<? super WR>>
        implements List<WR> {

    private final List<? extends OWR> list;
    private ListenerRegistration otherListListener;

    public ListBridge(BrokerBridgeResources resources, List<? extends OWR> list) {
        super(resources, new ListData(list.getId(), list.getName(), list.getDescription()));
        this.list = list;
    }

    public ListBridge(BrokerBridgeResources resources, List<? extends OWR> list, final Function<? super OWR, ? extends WR> converter) {
        this(resources, list);
        convert(converter);
    }

    public void convert(final Function<? super OWR, ? extends WR> converter) {
        otherListListener = list.addObjectListener(new ListListener<OWR>() {
            @Override
            public void elementAdded(OWR element) {
                addChild(converter.apply(element));
            }

            @Override
            public void elementRemoved(OWR element) {
                removeChild(element.getId());
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
    public void childObjectAdded(String childId, WR child) {
        super.childObjectAdded(childId, child);
        child.init(this);
        addLoadedBy(child);
        broadcastMessage(ADD_TYPE, child.getData().deepClone());
        for(ListListener<? super WR> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childId, WR child) {
        super.childObjectRemoved(childId, child);
        child.uninit();
        broadcastMessage(REMOVE_TYPE, child.getData());
        for(ListListener<? super WR> listener : getObjectListeners())
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
    public WR get(String name) {
        return getChild(name);
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<WR> iterator() {
        return getChildren().iterator();
    }
}
