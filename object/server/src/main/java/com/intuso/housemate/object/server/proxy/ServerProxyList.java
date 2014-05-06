package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Iterator;
import java.util.Map;

/**
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 */
public class ServerProxyList<
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ServerProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>>
        extends ServerProxyObject<ListData<CHILD_DATA>, CHILD_DATA, CHILD, ServerProxyList<CHILD_DATA, CHILD>, ListListener<? super CHILD>>
        implements List<CHILD> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    @Inject
    public ServerProxyList(Log log, ListenersFactory listenersFactory, Injector injector, @Assisted ListData<CHILD_DATA> data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        if(data instanceof ListData)
            for(Map.Entry<String, ? extends HousemateData<?>> entry : ((ListData<?>)data).getChildData().entrySet())
                getChild(entry.getKey()).copyValues(entry.getValue());
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD_TYPE, new Receiver<ClientPayload<HousemateData>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateData>> message) throws HousemateException {
                add(message.getPayload().getOriginal(), message.getPayload().getClient());
            }
        }));
        result.add(addMessageListener(REMOVE_TYPE, new Receiver<ClientPayload<HousemateData>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateData>> message) throws HousemateException {
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
    public void add(HousemateData data, RemoteClient clientId) throws HousemateException {
        // get the current child
        CHILD child = getChild(data.getId());

        // if there is one and the data is different, then remove the current one
        if(child != null) {
            if(!data.equals(child.getData())) {
                remove(data.getId());
                child = null;
            } else
                child.copyValues(data);
        }

        // if there isn't a child of that id, then add one
        if(child == null) {
            child = (CHILD) getInjector().getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
            child.init(ServerProxyList.this);
            child.setClient(clientId);
            addChild(child);
            for(ListListener<? super CHILD> listener : getObjectListeners())
                listener.elementAdded(child);
        }
    }

    /**
     * Removes an element from the list
     * @param id the id of the element to remove
     */
    public void remove(String id) {
        CHILD child = removeChild(id);
        if(child != null) {
            for(ListListener<? super CHILD> listener : getObjectListeners())
                listener.elementRemoved(child);
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
        return getChild(name);
    }

    @Override
    public int size() {
        return getChildren().size();
    }

    @Override
    public Iterator<CHILD> iterator() {
        return getChildren().iterator();
    }
}
