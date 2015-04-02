package com.intuso.housemate.api.comms;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Housemate client that can have clients of its own. Each program instance should use one of these if it expects to
 * have multiple clients of different types. Alternatively, this can be used to allow multiple clients to share a single
 * connection to the server, for example a selection of widgets on a mobile phone
 */
public abstract class Router implements Sender, Receiver {

    private final Log log;

    private final AtomicInteger nextId = new AtomicInteger(-1);
    private final Map<String, Receiver<?>> receivers = Maps.newConcurrentMap();

    private final RouterRoot root;

    private boolean fullyConnected = false;

    /**
     * @param log the log
     * @param listenersFactory
     */
    public Router(final Log log, ListenersFactory listenersFactory, PropertyRepository properties) {
        this.log = log;
        root = new RouterRoot(log, listenersFactory, properties, this);
        root.addObjectListener(new RootListener<RouterRoot>() {

            @Override
            public void serverConnectionStatusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus) {
                checkFullyConnected();
            }

            @Override
            public void applicationStatusChanged(RouterRoot root, ApplicationStatus applicationStatus) {
                checkFullyConnected();
            }

            @Override
            public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
                checkFullyConnected();
            }

            public void checkFullyConnected() {
                boolean fullyConnected = (root.getServerConnectionStatus() == ServerConnectionStatus.ConnectedToServer || root.getServerConnectionStatus() == ServerConnectionStatus.DisconnectedTemporarily)
                        && root.getApplicationInstanceStatus() == ApplicationInstanceStatus.Allowed;
                if(Router.this.fullyConnected != fullyConnected) {
                    Router.this.fullyConnected = fullyConnected;
                    Message<ServerConnectionStatus> message = new Message<>(new String[]{""},
                            Root.SERVER_CONNECTION_STATUS_TYPE,
                            fullyConnected ? root.getServerConnectionStatus() : ServerConnectionStatus.ConnectedToRouter);
                    for (Receiver receiver : receivers.values()) {
                        try {
                            receiver.messageReceived(message);
                        } catch (HousemateException e) {
                            log.e("Failed to notify client of new router status", e);
                        }
                    }
                }
            }

            @Override
            public void newApplicationInstance(RouterRoot root, String instanceId) {
                // do nothing
            }

            @Override
            public void newServerInstance(RouterRoot root, String serverId) {
                // do nothing
            }
        });
    }

    /**
     * Gets the log to use
     * @return the log to use
     */
    protected final Log getLog() {
        return log;
    }

    public ApplicationStatus getApplicationStatus() {
        return root.getApplicationStatus();
    }

    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return root.getApplicationInstanceStatus();
    }

    public ServerConnectionStatus getServerConnectionStatus() {
        return root.getServerConnectionStatus();
    }

    /**
     * Updates the router's connection status
     * @param serverConnectionStatus the router's new connection status
     */
    public final void setServerConnectionStatus(ServerConnectionStatus serverConnectionStatus) {
        root.setServerConnectionStatus(serverConnectionStatus);
    }

    /**
     * Adds a listener to the root object
     * @param listener the listener to add
     * @return the listener registration
     */
    public ListenerRegistration addObjectListener(RootListener<? super RouterRoot> listener) {
        return root.addObjectListener(listener);
    }

    /**
     * Logs in to the server
     */
    public void register(ApplicationDetails applicationDetails, String component) {
        if(!(getServerConnectionStatus() == ServerConnectionStatus.ConnectedToServer || getServerConnectionStatus() == ServerConnectionStatus.DisconnectedTemporarily))
            throw new HousemateRuntimeException("Cannot request access until the router is connected to the server");
        root.register(applicationDetails, component);
    }

    /**
     * Logs out of the server
     */
    public void unregister() {
        root.unregister();
    }

    /**
     * Makes a connection to the next router in the chain
     */
    public abstract void connect();

    /**
     * Disconnects from the next router in the chain
     */
    public abstract void disconnect();

    /**
     * Registers a new client connection
     * @param receiver the client's receiver implementation
     * @return a router registration that the client can use to send messages
     */
    public synchronized final Registration registerReceiver(Receiver receiver) {
        String clientId = "" + nextId.incrementAndGet();
        receivers.put(clientId, receiver);
        try {
            receiver.messageReceived(new Message<>(new String[] {""}, Root.SERVER_CONNECTION_STATUS_TYPE, root.getServerConnectionStatus()));
        } catch(HousemateException e) {
            log.e("Failed to tell new client " + clientId + " the current router status");
        }
        return new Registration(clientId);
    }

    @Override
    public void messageReceived(Message message) throws HousemateException {
        // get the key
        String key = message.getNextClientKey();

        // if no key then it's intended for this router's root object
        if(key == null) {
            root.distributeMessage(message);
        } else {
            Receiver receiver = receivers.get(key);
            if(receiver == null)
                root.unknownClient(key);
            else {
                try {
                    receiver.messageReceived(message);
                } catch(Throwable t) {
                    log.e("Receiver failed to process message", t);
                }
            }
        }
    }

    /**
     * Used by clients to send messages to the server
     */
    public final class Registration implements Sender {

        private boolean connected= true;
        private final String clientId;

        private Registration(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public void sendMessage(Message message) {
            if(!connected)
                throw new HousemateRuntimeException("No longer connected");
            message.addClientKey(clientId);
            Router.this.sendMessage(message);
        }

        /**
         * Removes this client registration
         */
        public synchronized final void unregister() {
            connected = false;
            if(clientId != null)
                receivers.remove(clientId);
        }

        /**
         * Notifies that this client's connection has been lost
         */
        public synchronized final void connectionLost() {
            connected = false;
            if(clientId != null)
                receivers.remove(clientId);
            Router.this.sendMessage(new Message<>(new String[] {""}, Root.CONNECTION_LOST_TYPE,
                    new StringPayload(clientId)));
        }
    }
}
