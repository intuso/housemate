package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;

import java.util.Iterator;

/**
 */
public class ListBridge<
            WBL extends HousemateData<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends BridgeObject<ListData<WBL>, WBL, WR, ListBridge<WBL, WR>, ListListener<? super WR>>
        implements List<WR> {

    public ListBridge(Log log, ListenersFactory listenersFactory, ListData<WBL> data) {
        super(log, listenersFactory, data);
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
    public void childObjectAdded(String childId, WR child) {
        child.init(this);
        addLoadedBy(child);
        broadcastMessage(ADD_TYPE, child.getData().deepClone());
        for(ListListener<? super WR> listener : getObjectListeners())
            listener.elementAdded(child);
        super.childObjectAdded(childId, child);
    }

    @Override
    public void childObjectRemoved(String childId, WR child) {
        child.uninit();
        broadcastMessage(REMOVE_TYPE, child.getData());
        for(ListListener<? super WR> listener : getObjectListeners())
            listener.elementRemoved(child);
        super.childObjectRemoved(childId, child);
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

    public void add(WR child) {
        super.addChild(child);
    }
}
