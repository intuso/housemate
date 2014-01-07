package com.intuso.housemate.object.server.proxy;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.housemate.object.server.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
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
        extends HousemateObject<DATA, CHILD_DATA, CHILD, LISTENER>
        implements RemoteClientListener {

    private final Injector injector;

    private RemoteClient client;
    private ListenerRegistration clientListener;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected ServerProxyObject(Log log, Injector injector, DATA data) {
        super(log, data);
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }

    /**
     * Sets the client that this object is a proxy to
     * @param client the client
     * @throws HousemateException
     */
    public void setClient(RemoteClient client) throws HousemateException {
        if(this.client != null)
            throw new HousemateException("This object already has a client");
        else if(client.getType() != ConnectionType.Real)
            throw new HousemateException("Client is not of type " + ConnectionType.Real);
        if(clientListener != null)
            clientListener.removeListener();
        this.client = client;
        clientListener = client.addListener(this);
        for(ServerProxyObject<?, ?, ?, ?, ?> child : getChildren())
                ((ServerProxyObject)child).setClient(client);
    }

    /**
     * Sends a message to the client this object is a proxy to
     * @param type the type of the message
     * @param payload the message payload
     * @throws HousemateException if an error occurs sending the message, for example the client is not connected
     */
    protected final void sendMessage(String type, Message.Payload payload) throws HousemateException {
        if(client == null)
            throw new HousemateException("Client has disconnected. This object should no longer be used");
        else if(!client.isCurrentlyConnected())
            throw new HousemateException("Client is not currently connected");
        else
            client.sendMessage(getPath(), type, payload);
    }

    @Override
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?> parent) {
        // unwrap children
        try {
            createChildren(new ObjectFactory<CHILD_DATA, CHILD, HousemateException>() {
                @Override
                public CHILD create(CHILD_DATA data) throws HousemateException {
                    return (CHILD) injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
                }
            });
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Failed to unwrap child object", e);
        }
    }

    @Override
    protected void initPostRecurseHook(HousemateObject<?, ?, ?, ?> parent) {
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
    public void disconnected(RemoteClient client) {
        clientListener.removeListener();
        this.client = null;
    }

    @Override
    public void connectionLost(RemoteClient client) {
        // do nothing
    }

    @Override
    public void reconnected(RemoteClient client) {
        // do nothing
    }
}
