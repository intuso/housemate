package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectListener;

import java.util.Iterator;

/**
 * @param <CHILD_DATA> the type of the child's data object
 * @param <CHILD> the type of the child
 * @param <LIST> the type of the list
 */
public abstract class ProxyList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            LIST extends ProxyList<CHILD_DATA, CHILD, LIST>>
        extends ProxyObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, LIST, ListListener<? super CHILD>>
        implements List<CHILD>, ObjectListener<CHILD> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyList(Log log, ListenersFactory listenersFactory, ListData data) {
        super(log, listenersFactory, data);
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
    protected java.util.List registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD_TYPE, new Receiver<CHILD_DATA>() {
            @Override
            public void messageReceived(Message<CHILD_DATA> message) throws HousemateException {
                CHILD child = createChildInstance(message.getPayload());
                if(child != null) {
                    child.init(ProxyList.this);
                    addChild(child);
                } else
                    throw new HousemateException("Could not create new list element");
            }
        }));
        result.add(addMessageListener(REMOVE_TYPE, new Receiver<HousemateData>() {
            @Override
            public void messageReceived(Message<HousemateData> message) throws HousemateException {
                CHILD child = getChild(message.getPayload().getId());
                if(child != null) {
                    child.uninit();
                    removeChild(child.getId());
                }
            }
        }));
        return result;
    }

    @Override
    public final CHILD get(String id) {
        return getChild(id);
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getChildren().iterator();
    }

    @Override
    public void childObjectAdded(String childId, CHILD child) {
        super.childObjectAdded(childId, child);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(child);
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        super.childObjectRemoved(childId, child);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementRemoved(child);
    }
}