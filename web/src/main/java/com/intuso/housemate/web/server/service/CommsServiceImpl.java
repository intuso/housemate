package com.intuso.housemate.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.web.client.NotConnectedException;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.server.ContextListener;
import com.intuso.housemate.web.server.GWTClientEndpoint;

import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class CommsServiceImpl extends RemoteServiceServlet implements CommsService {

    private final static String ATT_NAME = "endpoint";

    @Override
    public void connectClient() {
        createNewEndpoint();
    }

    @Override
    public void disconnectClient() {
        removeEndpoint();
    }

    @Override
    public Message<Message.Payload>[] getMessages(int num, long timeout) throws NotConnectedException {
        GWTClientEndpoint endpoint = getEndpoint();
        if(endpoint == null)
            throw new NotConnectedException();
        List<Message> result = endpoint.getMessages(num, timeout);
        return result.toArray(new Message[result.size()]);
    }

    @Override
    public void sendMessageToBroker(Message<Message.Payload> message) {
        getEndpoint().sendMessage(message);
    }

    private GWTClientEndpoint getEndpoint() {
        return (GWTClientEndpoint)this.getThreadLocalRequest().getSession().getAttribute(ATT_NAME);
    }

    private void createNewEndpoint() {
        GWTClientEndpoint endpoint = getEndpoint();
        if(endpoint != null)
            endpoint.disconnect();
        this.getThreadLocalRequest().getSession().setAttribute(ATT_NAME, new GWTClientEndpoint(
                ContextListener.RESOURCES.getRouter()));
    }

    private void removeEndpoint() {
        this.getThreadLocalRequest().getSession().removeAttribute(ATT_NAME);
    }
}
