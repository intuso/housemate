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
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/07/12
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyList<SWBL extends HousemateObjectWrappable<?>,
            SWR extends BrokerProxyObject<? extends SWBL, ?, ?, ?, ?>>
        extends BrokerProxyObject<ListWrappable<SWBL>, SWBL, SWR, BrokerProxyList<SWBL, SWR>, ListListener<? super SWR>>
        implements List<SWR> {

    public BrokerProxyList(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, ? extends SWR>> resources, ListWrappable<SWBL> listWrappable) {
        super(resources, listWrappable);
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD, new Receiver<ClientPayload<HousemateObjectWrappable>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateObjectWrappable>> message) throws HousemateException {
                add(message.getPayload().getOriginal(), message.getPayload().getClient());
            }
        }));
        result.add(addMessageListener(REMOVE, new Receiver<ClientPayload<HousemateObjectWrappable>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateObjectWrappable>> message) throws HousemateException {
                remove(message.getPayload().getOriginal().getId());
            }
        }));
        return result;
    }

    public void add(HousemateObjectWrappable wrappable, RemoteClient clientId) throws HousemateException {
        SWR wrapper = null;
        try {
            wrapper = getResources().getFactory().create(getResources(), (SWBL)wrappable);
        } catch(HousemateException e) {
            throw new HousemateException("Failed to create new list element", e);
        }
        wrapper.init(BrokerProxyList.this);
        wrapper.setClient(clientId);
        addWrapper(wrapper);
        for(ListListener<? super SWR> listener : getObjectListeners())
            listener.elementAdded(wrapper);
    }

    public void remove(String id) {
        SWR wrapper = removeWrapper(id);
        if(wrapper != null) {
            for(ListListener<? super SWR> listener : getObjectListeners())
                listener.elementRemoved(wrapper);
        }
    }

    @Override
    public ListenerRegistration addObjectListener(ListListener<? super SWR> listener, boolean callForExistingElements) {
        ListenerRegistration listenerRegistration = addObjectListener(listener);
        if(callForExistingElements)
            for(SWR element : this)
                listener.elementAdded(element);
        return listenerRegistration;
    }

    @Override
    public final SWR get(String name) {
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
