package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.utilities.listener.ListenerRegistration;

public interface RemoteClient {

    /**
     * Gets the type of the remote client
     * @return the remote client's type
     */
    ConnectionType getType();

    /**
     * Gets the connection id of the remote client
     * @return the connection id of the remote client
     */
    String getConnectionId();

    /**
     * Sends a message to the client
     * @param path the path to the object the message is for
     * @param type the type of the message
     * @param payload the message payload
     * @throws HousemateException if the message fails to be sent
     */
    void sendMessage(String[] path, String type, Message.Payload payload) throws HousemateException;

    /**
     * Gets if the client is currently connected
     * @return true if the client is connected, otherwise false
     */
    boolean isCurrentlyConnected();

    /**
     * Adds a listener for updates about the remote client's connection status
     * @param listener the listener to add
     * @return listener registration
     */
    ListenerRegistration addListener(RemoteClientListener listener);
}
