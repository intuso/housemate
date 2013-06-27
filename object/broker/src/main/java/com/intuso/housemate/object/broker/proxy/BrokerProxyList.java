package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.Iterator;

/**
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 */
public class BrokerProxyList<
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends BrokerProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>>
        extends BrokerProxyObject<ListWrappable<CHILD_DATA>, CHILD_DATA, CHILD, BrokerProxyList<CHILD_DATA, CHILD>, ListListener<? super CHILD>>
        implements List<CHILD> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyList(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, CHILD_DATA, ? extends CHILD>> resources, ListWrappable<CHILD_DATA> data) {
        super(resources, data);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD_TYPE, new Receiver<ClientPayload<HousemateObjectWrappable>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateObjectWrappable>> message) throws HousemateException {
                add(message.getPayload().getOriginal(), message.getPayload().getClient());
            }
        }));
        result.add(addMessageListener(REMOVE_TYPE, new Receiver<ClientPayload<HousemateObjectWrappable>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateObjectWrappable>> message) throws HousemateException {
                remove(message.getPayload().getOriginal().getId());
            }
        }));
        return result;
    }

    /**
     * Adds an element to the list
     * @param data the data for the new object
     * @param clientId the id of the client
     * @throws HousemateException
     */
    public void add(HousemateObjectWrappable data, RemoteClient clientId) throws HousemateException {
        CHILD wrapper = null;
        try {
            wrapper = getResources().getFactory().create(getResources(), (CHILD_DATA)data);
        } catch(HousemateException e) {
            throw new HousemateException("Failed to create new list element", e);
        }
        wrapper.init(BrokerProxyList.this);
        wrapper.setClient(clientId);
        addWrapper(wrapper);
        for(ListListener<? super CHILD> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    /**
     * Removes an element from the list
     * @param id the id of the element to remove
     */
    public void remove(String id) {
        CHILD wrapper = removeWrapper(id);
        if(wrapper != null) {
            for(ListListener<? super CHILD> listener : getObjectListeners())
                listener.elementRemoved(wrapper);
        }
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
    public final CHILD get(String name) {
        return getWrapper(name);
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
