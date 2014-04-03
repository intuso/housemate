package com.intuso.housemate.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
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
        List<Message> result = getEndpoint().getMessages(num, timeout);
        return result.toArray(new Message[result.size()]);
    }

    @Override
    public void sendMessageToServer(Message<Message.Payload> message) throws NotConnectedException {
        getEndpoint().sendMessage(message);
    }

    private GWTClientEndpoint getEndpoint() throws NotConnectedException {
        if(getThreadLocalRequest().getSession().getAttribute(ATT_NAME) != null
                && getThreadLocalRequest().getSession().getAttribute(ATT_NAME) instanceof GWTClientEndpoint)
            return (GWTClientEndpoint)getThreadLocalRequest().getSession().getAttribute(ATT_NAME);
        throw new NotConnectedException();
    }

    private void createNewEndpoint() {
        try {
            getEndpoint().disconnect();
        // don't care if it didn't exist
        } catch (NotConnectedException e) {}
        this.getThreadLocalRequest().getSession().setAttribute(ATT_NAME,
                new GWTClientEndpoint(
                        ContextListener.INJECTOR.getInstance(Router.class)));
    }

    private void removeEndpoint() {
        this.getThreadLocalRequest().getSession().removeAttribute(ATT_NAME);
    }
}
