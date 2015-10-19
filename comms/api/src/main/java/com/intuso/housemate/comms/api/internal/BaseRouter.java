package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.StringPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Housemate client that can have clients of its own. Each program instance should use one of these if it expects to
 * have multiple clients of different types. Alternatively, this can be used to allow multiple clients to share a single
 * connection to the server, for example a selection of widgets on a mobile phone
 */
public abstract class BaseRouter<ROUTER extends BaseRouter> implements Router<ROUTER> {

    private final Log log;

    private final AtomicInteger nextId = new AtomicInteger(-1);
    private final Listeners<ClientConnection.Listener<? super ROUTER>> listeners;
    private final MessageDistributor messageDistributor;
    private final MessageSequencer messageSequencer;
    private final Map<String, Message.Receiver<?>> receivers = new ConcurrentHashMap<>();

    private String routerId;
    private ServerConnectionStatus routerServerConnectionStatus = null;
    private ServerConnectionStatus serverConnectionStatus;
    private ServerConnectionStatus listenerToldServerConnectionStatus;

    /**
     * @param log the log
     * @param listenersFactory
     *
     */
    public BaseRouter(final Log log, ListenersFactory listenersFactory) {
        this(log, listenersFactory, null);
    }

    /**
     * @param log the log
     * @param listenersFactory
     * @param routerServerConnectionStatus
     */
    public BaseRouter(final Log log, ListenersFactory listenersFactory, ServerConnectionStatus routerServerConnectionStatus) {
        this.log = log;
        this.listeners = listenersFactory.create();
        this.routerServerConnectionStatus = routerServerConnectionStatus;
        this.messageDistributor = new MessageDistributor(listenersFactory);
        this.messageSequencer = new MessageSequencer(messageDistributor);
        messageDistributor.registerReceiver(Router.ROUTER_ID, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                routerId = message.getPayload().getValue();
                setServerConnectionStatus(BaseRouter.this.routerServerConnectionStatus, ServerConnectionStatus.ConnectedToServer);
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
    protected final Log getLog() {
        return log;
    }

    protected final Listeners<ClientConnection.Listener<? super ROUTER>> getListeners() {
        return listeners;
    }

    protected ListenerRegistration registerRouterReceiver(String type, Message.Receiver<?> receiver) {
        return messageDistributor.registerReceiver(type, receiver);
    }

    protected void setRouterServerConnectionStatus(ServerConnectionStatus routerServerConnectionStatus) {
        setServerConnectionStatus(routerServerConnectionStatus, serverConnectionStatus);
    }

    public ServerConnectionStatus getServerConnectionStatus() {
        return serverConnectionStatus;
    }

    private synchronized void setServerConnectionStatus(ServerConnectionStatus routerServerConnectionStatus, ServerConnectionStatus serverConnectionStatus) {
        this.routerServerConnectionStatus = routerServerConnectionStatus;
        this.serverConnectionStatus = serverConnectionStatus;
        if (routerServerConnectionStatus == ServerConnectionStatus.ConnectedToServer)
            tellRegistrations(serverConnectionStatus);
        else if(routerServerConnectionStatus == null)
            tellRegistrations(ServerConnectionStatus.DisconnectedPermanently);
        else
            tellRegistrations(ServerConnectionStatus.DisconnectedTemporarily);
    }

    private synchronized void tellRegistrations(ServerConnectionStatus status) {
        if(listenerToldServerConnectionStatus != status) {
            listenerToldServerConnectionStatus = status;
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
            receiver.serverConnectionStatusChanged(getThis(), serverConnectionStatus);
        } catch(HousemateCommsException e) {
            log.e("Failed to tell new client " + clientId + " the current router status", e);
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
                    log.e("Receiver failed to process message", t);
                }
            }
        }
    }

    protected void connecting() {
        if(routerId != null)
            setServerConnectionStatus(routerServerConnectionStatus, ServerConnectionStatus.Connecting);
    }

    protected void connectionEstablished() {
        if(routerId != null) {
            getLog().d("Router re-registering");
            sendMessageNow(new Message<>(AccessManager.ROOT_PATH, Router.ROUTER_CONNECTED, new StringPayload(routerId)));
        } else {
            sendMessageNow(new Message<>(AccessManager.ROOT_PATH, Router.ROUTER_CONNECTED, new StringPayload(null)));
            setServerConnectionStatus(routerServerConnectionStatus, ServerConnectionStatus.ConnectedToRouter);
        }
    }

    protected void connectionLost(boolean temporary) {
        // set the connection status
        if(temporary) {
            setServerConnectionStatus(routerServerConnectionStatus, ServerConnectionStatus.DisconnectedTemporarily);
        } else {
            setServerConnectionStatus(routerServerConnectionStatus, ServerConnectionStatus.DisconnectedPermanently);
            routerServerConnectionStatus = null;
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
