package com.intuso.housemate.object.server.client;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.object.real.RealApplication;
import com.intuso.housemate.object.real.RealApplicationInstance;
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
     * Adds a listener for updates about the remote client's connection status
     * @param listener the listener to add
     * @return listener registration
     */
    ListenerRegistration addListener(RemoteClientListener listener);

    public void setApplicationAndInstance(RealApplication application, RealApplicationInstance applicationInstance);
}
