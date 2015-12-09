package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.StringPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Housemate client that can have clients of its own. Each program instance should use one of these if it expects to
 * have multiple clients of different types. Alternatively, this can be used to allow multiple clients to share a single
 * connection to the server, for example a selection of widgets on a mobile phone
 */
public abstract class BaseRouter<ROUTER extends BaseRouter> implements Router<ROUTER> {

    private final Logger logger;

    private final AtomicInteger nextId = new AtomicInteger(-1);
    private final Listeners<ClientConnection.Listener<? super ROUTER>> listeners;
    private final MessageDistributor messageDistributor;
    private final MessageSequencer messageSequencer;
    private final Map<String, Message.Receiver<?>> receivers = new ConcurrentHashMap<>();

    private String routerId;
    private String serverInstanceId;
    private ConnectionStatus connectionStatus = ConnectionStatus.DisconnectedPermanently;
    private ConnectionStatus thisConnectionStatus = ConnectionStatus.DisconnectedPermanently;
    private ConnectionStatus nextConnectionStatus = ConnectionStatus.DisconnectedPermanently;
    private ConnectionStatus listenerToldConnectionStatus;

    /**
     * @param logger the log
     * @param listenersFactory
     */
    public BaseRouter(final Logger logger, ListenersFactory listenersFactory) {
        this.logger = logger;
        this.listeners = listenersFactory.create();
        this.messageDistributor = new MessageDistributor(listenersFactory);
        this.messageSequencer = new MessageSequencer(messageDistributor);
        messageDistributor.registerReceiver(Router.ROUTER_ID, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                routerId = message.getPayload().getValue();
                setConnectionStatuses(ConnectionStatus.ConnectedToServer, ConnectionStatus.ConnectedToServer);
            }
        });
        registerRouterReceiver(ClientConnection.SERVER_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                if (!serverInstanceId.equals(message.getPayload().getValue())) {
                    serverInstanceId = message.getPayload().getValue();
                    for (ClientConnection.Listener<? super ROUTER> listener : getListeners())
                        listener.newServerInstance(getThis(), serverInstanceId);
                }
            }
        });
        registerRouterReceiver(ClientConnection.NEXT_CONNECTION_STATUS_TYPE, new Message.Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) {
                setNextConnectionStatus(message.getPayload());
            }
        });
    }

    public ROUTER getThis() {
        return (ROUTER) this;
    }

    /**
     * Gets the log to use
     * @return the log to use
     */
    protected final Logger getLogger() {
        return logger;
    }

    protected final Listeners<ClientConnection.Listener<? super ROUTER>> getListeners() {
        return listeners;
    }

    protected ListenerRegistration registerRouterReceiver(String type, Message.Receiver<?> receiver) {
        return messageDistributor.registerReceiver(type, receiver);
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public ConnectionStatus getThisConnectionStatus() {
        return thisConnectionStatus;
    }

    private void setThisConnectionStatus(ConnectionStatus thisConnectionStatus) {
        setConnectionStatuses(thisConnectionStatus, nextConnectionStatus);
    }

    public ConnectionStatus getNextConnectionStatus() {
        return nextConnectionStatus;
    }

    private void setNextConnectionStatus(ConnectionStatus nextConnectionStatus) {
        setConnectionStatuses(thisConnectionStatus, nextConnectionStatus);
    }

    private synchronized void setConnectionStatuses(ConnectionStatus thisConnectionStatus, ConnectionStatus nextConnectionStatus) {
        if(this.thisConnectionStatus == thisConnectionStatus && this.nextConnectionStatus == nextConnectionStatus)
            return;

        this.thisConnectionStatus = thisConnectionStatus;
        this.nextConnectionStatus = nextConnectionStatus;

        switch (thisConnectionStatus) {
            case ConnectedToServer:
                switch (nextConnectionStatus) {
                    case ConnectedToServer:
                        tellRegistrations(ConnectionStatus.ConnectedToServer);
                        break;
                    default:
                        tellRegistrations(ConnectionStatus.ConnectedToRouter);
                }
                break;
            default:
                tellRegistrations(thisConnectionStatus);
        }
    }

    private synchronized void tellRegistrations(ConnectionStatus status) {
        connectionStatus = status;
        if(listenerToldConnectionStatus != status) {
            listenerToldConnectionStatus = status;
            for(ClientConnection.Listener<? super ROUTER> listener : listeners)
                listener.serverConnectionStatusChanged(getThis(), status);
        }
    }

    /**
     * Adds a listener to the root object
     * @param listener the listener to add
     * @return the listener registration
     */
    @Override
    public ListenerRegistration addListener(Listener<? super ROUTER> listener) {
        return listeners.addListener(listener);
    }

    /**
     * Registers a new client connection
     * @param receiver the client's receiver implementation
     * @return a router registration that the client can use to send messages
     */
    public synchronized final Registration registerReceiver(Receiver<ROUTER> receiver) {
        String clientId = "" + nextId.incrementAndGet();
        receivers.put(clientId, receiver);
        try {
            receiver.serverConnectionStatusChanged(getThis(), nextConnectionStatus);
        } catch(HousemateCommsException e) {
            logger.error("Failed to tell new client " + clientId + " the current router status", e);
        }
        return new RegistrationImpl(clientId);
    }

    @Override
    public void messageReceived(Message message) throws HousemateCommsException {
        // get the key
        String key = message.getNextClientKey();

        // if no key then it's intended for this router's root object
        if(key == null) {
            if(message.getSequenceId() != null)
                sendMessage(new Message<Message.Payload>(new String[] {""}, Message.RECEIVED_TYPE, new Message.ReceivedPayload(message.getSequenceId())));
            messageSequencer.messageReceived(message);
        } else {
            Message.Receiver receiver = receivers.get(key);
            if(receiver == null)
                sendMessage(new Message<Message.Payload>(new String[] {""}, ClientConnection.UNKNOWN_CLIENT_ID, new StringPayload(key)));
            else {
                try {
                    receiver.messageReceived(message);
                } catch(Throwable t) {
                    logger.error("Receiver failed to process message", t);
                }
            }
        }
    }

    protected void connecting() {
        if(routerId == null)
            setThisConnectionStatus(ConnectionStatus.Connecting);
    }

    protected void connectionEstablished() {
        if(routerId != null) {
            getLogger().debug("Router re-registering");
            sendMessageNow(new Message<>(AccessManager.ROOT_PATH, Router.ROUTER_CONNECTED, new StringPayload(routerId)));
        } else {
            setThisConnectionStatus(ConnectionStatus.ConnectedToRouter);
            sendMessageNow(new Message<>(AccessManager.ROOT_PATH, Router.ROUTER_CONNECTED, new StringPayload(null)));
        }
    }

    protected void connectionLost(boolean temporary) {
        // set the connection status
        if(temporary) {
            setThisConnectionStatus(ConnectionStatus.DisconnectedTemporarily);
        } else {
            setThisConnectionStatus(ConnectionStatus.DisconnectedPermanently);
            setNextConnectionStatus(null);
            routerId = null;
        }
    }

    protected abstract void sendMessageNow(Message<?> message);

    /**
     * Used by clients to send messages to the server
     */
    final class RegistrationImpl implements Registration {

        private boolean connected= true;
        private final String clientId;

        public RegistrationImpl(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public void sendMessage(Message message) {
            if(!connected)
                throw new HousemateCommsException("No longer connected");
            message.addClientKey(clientId);
            BaseRouter.this.sendMessage(message);
        }

        /**
         * Removes this client registration
         */
        public synchronized final void unregister() {
            connected = false;
            if(clientId != null)
                BaseRouter.this.receivers.remove(clientId);
        }
    }
}
