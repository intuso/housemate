package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 */
public interface RemoteClient {
    ConnectionType getType();
    String getConnectionId();
    void sendMessage(String[] path, String type, Message.Payload payload) throws HousemateException;
    boolean isCurrentlyConnected();
    ListenerRegistration addListener(RemoteClientListener listener);
}
