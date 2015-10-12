package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * A Housemate client that can have clients of its own. Each program instance should use one of these if it expects to
 * have multiple clients of different types. Alternatively, this can be used to allow multiple clients to share a single
 * connection to the server, for example a selection of widgets on a mobile phone
 */
public interface Router<ROUTER extends Router> extends ClientConnection, Message.Sender, Message.Receiver<Message.Payload> {

    String ROUTER_CONNECTED = "router-connected";
    String ROUTER_ID = "router-id";

    ServerConnectionStatus getServerConnectionStatus();

    /**
     * Adds a listener to the root object
     * @param listener the listener to add
     * @return the listener registration
     */
    ListenerRegistration addListener(Listener<? super ROUTER> listener);

    /**
     * Registers a new client connection
     * @param receiver the client's receiver implementation
     * @return a router registration that the client can use to send messages
     */
    Registration registerReceiver(Message.Receiver<Message.Payload> receiver);

    interface Listener<ROUTER extends Router> extends ClientConnection.Listener<ROUTER> {}

    /**
     * Used by clients to send messages to the server
     */
    interface Registration extends Message.Sender {

        /**
         * Removes this client registration
         */
        void unregister();
    }
}
