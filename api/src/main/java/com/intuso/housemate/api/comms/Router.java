package com.intuso.housemate.api.comms;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Housemate client that can have clients of its own. Each program instance should use one of these if it expects to
 * have multiple clients of different types. Alternatively, this can be used to allow multiple clients to share a single
 * connection to the broker, for example a selection of widgets on a mobile phone
 */
public abstract class Router implements Sender, Receiver {

    /**
     * Enum of possible connection statuses for a router
     */
    protected enum Status {
        Disconnected,
        Connecting,
        Connected
    }

    private final Log log;

    private final AtomicInteger nextId = new AtomicInteger(-1);
    private final Map<String, Receiver<?>> receivers = Maps.newConcurrentMap();

    private final RouterRootObject root;

    private Status routerStatus = Status.Disconnected;

    /**
     * @param resources the resources
     */
    public Router(Resources resources) {
        this.log = resources.getLog();
        root = new RouterRootObject(resources, this);
        root.addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
                Message<ConnectionStatus> message = new Message<ConnectionStatus>(new String[] {""}, Root.STATUS_TYPE, status);
                for(Receiver receiver : receivers.values()) {
                    try {
                        receiver.messageReceived(message);
                    } catch(HousemateException e) {
                        log.e("Failed to notify client of new router status");
                        log.st(e);
                    }
                }
            }

            @Override
            public void brokerInstanceChanged(RouterRootObject root) {
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

    /**
     * Updates the router's connection status
     * @param routerStatus the router's new connection status
     */
    protected final void setRouterStatus(Status routerStatus) {
        this.routerStatus = routerStatus;
        root.setRouterStatus(routerStatus);
    }

    /**
     * Adds a listener to the root object
     * @param listener the listener to add
     * @return the listener registration
     */
    public ListenerRegistration addObjectListener(RootListener<? super RouterRootObject> listener) {
        return root.addObjectListener(listener);
    }

    /**
     * Logs in to the broker
     * @param method the method to authenticate with
     */
    public final void login(AuthenticationMethod method) {
        if(routerStatus != Status.Connected)
            throw new HousemateRuntimeException("Cannot login until the router is connected");
        root.login(method);
    }

    /**
     * Logs out of the broker
     */
    public final void logout() {
        root.logout();
    }

    /**
     * Makes a connection to the next router in the chain, or the broker itself
     */
    public abstract void connect();

    /**
     * Disconnects from the next router in the chain or the broker itself
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
            receiver.messageReceived(new Message<ConnectionStatus>(new String[] {""}, Root.STATUS_TYPE, root.getStatus()));
        } catch(HousemateException e) {
            log.e("Failed to tell new client " + clientId + " the current router status");
        }
        return new Registration(clientId);
    }

    @Override
    public final void messageReceived(Message message) throws HousemateException {
        // get the key
        String key = message.getNextClientKey();

        // if no key then it's intended for this router's root object
        if(key == null) {
            root.distributeMessage(message);
        } else {
            Receiver receiver = receivers.get(key);
            if(receiver == null)
                root.unknownClient(key);
            else
                receiver.messageReceived(message);
        }
    }

    /**
     * Used by clients to send messages to the broker
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
        public synchronized final void remove() {
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
            Router.this.sendMessage(new Message<StringPayload>(new String[] {""}, Root.CONNECTION_LOST_TYPE,
                    new StringPayload(clientId)));
        }
    }
}
