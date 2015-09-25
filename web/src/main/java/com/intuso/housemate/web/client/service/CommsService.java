package com.intuso.housemate.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.web.client.NotConnectedException;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("comms")
public interface CommsService extends RemoteService {
    void connectClient();
    void disconnectClient();
    Message<Message.Payload>[] getMessages(int num, long timeout) throws NotConnectedException;
    void sendMessageToServer(Message<Message.Payload> message) throws NotConnectedException;
    void resetPassword(String username);
    Boolean checkCredentials(String username, String password);
}
