package com.intuso.housemate.object.server;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.utilities.listener.ListenerRegistration;

public interface RemoteClient {

    ClientInstance getClientInstance();

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

    public void setStatus(ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus);
}
