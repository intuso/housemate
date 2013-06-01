package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.connection.ClientWrappable;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 30/05/13
 * Time: 09:45
 * To change this template use File | Settings | File Templates.
 */
public interface RemoteClient {
    ClientWrappable.Type getType();
    String getConnectionId();
    void sendMessage(String[] path, String type, Message.Payload payload) throws HousemateException;
    boolean isCurrentlyConnected();
    ListenerRegistration addListener(RemoteClientListener listener);
}
