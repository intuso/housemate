package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Sender;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 20/05/13
 * Time: 23:03
 * To change this template use File | Settings | File Templates.
 */
public interface ServerComms extends Sender, Receiver {
    void start();
    void disconnect();
    void sendMessageToClient(String[] path, String type, Message.Payload payload, RemoteClient client) throws HousemateException;
}
