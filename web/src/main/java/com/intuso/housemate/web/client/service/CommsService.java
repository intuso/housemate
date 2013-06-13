package com.intuso.housemate.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.web.client.NotConnectedException;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("comms")
public interface CommsService extends RemoteService {
    void connectClient();
    void disconnectClient();
    Message<Message.Payload>[] getMessages(int num, long timeout) throws NotConnectedException;
    void sendMessageToBroker(Message<Message.Payload> message);
}
