package com.intuso.housemate.server.object.proxy;

import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.RemoteLinkedObject;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.object.api.internal.ObjectListener;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.housemate.server.comms.RemoteClient;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 * @param <OBJECT> the type of the object
 * @param <LISTENER> the type of the listener
 */
public abstract class ServerProxyObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ServerProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends ServerProxyObject<?, ?, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends RemoteLinkedObject<DATA, CHILD_DATA, CHILD, LISTENER> {

    private final ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory;
    private ServerProxyRoot root;

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected ServerProxyObject(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, DATA data) {
        super(log, listenersFactory, data);
        this.objectFactory = objectFactory;
    }

    @Override
    protected void initPreRecurseHook(RemoteLinkedObject<?, ?, ?, ?> parent) {

        if((ServerProxyObject)this instanceof ServerProxyRoot)
            root = (ServerProxyRoot)(ServerProxyObject)this;
        else if(parent != null && parent instanceof ServerProxyObject)
            root = ((ServerProxyObject)parent).root;

        // unwrap children
        try {
            createChildren(new ObjectFactory<CHILD_DATA, CHILD>() {
                @Override
                public CHILD create(CHILD_DATA data) {
                    return (CHILD) objectFactory.create(data);
                }
            });
        } catch(Throwable t) {
            throw new HousemateCommsException("Failed to unwrap child object", t);
        }
    }

    @Override
    protected void initPostRecurseHook(RemoteLinkedObject<?, ?, ?, ?> parent) {
        getChildObjects();
    }

    /**
     * Gets the child objects required by this object
     */
    protected void getChildObjects() {}

    /**
     * Gets this object as an instance of its actual class
     * @return
     */
    protected final OBJECT getThis() {
        return (OBJECT)this;
    }

    @Override
    protected java.util.List<ListenerRegistration> registerListeners() {
        java.util.List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ADD_TYPE, new Message.Receiver<ClientPayload<HousemateData>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateData>> message) {
                add(message.getPayload().getOriginal(), message.getPayload().getClient());
            }
        }));
        result.add(addMessageListener(REMOVE_TYPE, new Message.Receiver<ClientPayload<HousemateData>>() {
            @Override
            public void messageReceived(Message<ClientPayload<HousemateData>> message) {
                removeChild(message.getPayload().getOriginal().getId());
            }
        }));
        return result;
    }

    /**
     * Adds an element to the list
     * @param data the data for the new object
     * @param clientId the id of the client
     */
    public void add(HousemateData data, RemoteClient clientId) {
        // get the current child
        CHILD child = getChild(data.getId());

        // if there is one and the data is different, then remove the current one
        if(child != null) {
            if(!data.equals(child.getData())) {
                removeChild(data.getId());
                child = null;
            } else
                child.copyValues(data);
        }

        // if there isn't a child of that id, then add one
        if(child == null) {
            child = (CHILD) objectFactory.create(data);
            child.init(ServerProxyObject.this);
            addChild(child);
        }
    }

    /**
     * Sends a message to the client this object is a proxy to
     * @param type the type of the message
     * @param payload the message payload
     */
    protected final void sendMessage(String type, Message.Payload payload) {
        root.sendMessage(getPath(), type, payload);
    }

    protected abstract void copyValues(HousemateData<?> data);
}
