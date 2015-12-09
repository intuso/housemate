package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Iterator;

/**
 */
public class ListBridge<
            WBL extends HousemateData<?>,
            WR extends BridgeObject<? extends WBL, ?, ?, ?, ?>>
        extends BridgeObject<ListData<WBL>, WBL, WR, ListBridge<WBL, WR>, List.Listener<? super WR>>
        implements List<WR> {

    public ListBridge(Logger logger, ListenersFactory listenersFactory, ListData<WBL> data) {
        super(logger, listenersFactory, data);
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super WR> listener, boolean callForExistingElements) {
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
        for(List.Listener<? super WR> listener : getObjectListeners())
            listener.elementAdded(child);
        super.childObjectAdded(childId, child);
    }

    @Override
    public void childObjectRemoved(String childId, WR child) {
        child.uninit();
        broadcastMessage(REMOVE_TYPE, child.getData());
        for(List.Listener<? super WR> listener : getObjectListeners())
            listener.elementRemoved(child);
        super.childObjectRemoved(childId, child);
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
