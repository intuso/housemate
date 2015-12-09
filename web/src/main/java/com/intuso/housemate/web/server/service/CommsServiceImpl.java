package com.intuso.housemate.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.object.v1_0.api.TypeInstance;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.persistence.v1_0.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.v1_0.api.Persistence;
import com.intuso.housemate.web.client.NotConnectedException;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.server.ContextListener;
import com.intuso.housemate.web.server.GWTClientEndpoint;
import org.slf4j.Logger;

import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class CommsServiceImpl extends RemoteServiceServlet implements CommsService {

    private final static String ATT_NAME = "endpoint";

    private final Logger logger;
    private final Persistence persistence;

    @Inject
    public CommsServiceImpl() {
        this.logger = ContextListener.INJECTOR.getInstance(Logger.class);
        this.persistence = ContextListener.INJECTOR.getInstance(Persistence.class);
    }

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
    public void sendMessageToServer(Message<?> message) throws NotConnectedException {
        getEndpoint().sendMessage(message);
    }

    public void resetPassword(String username) {
        try {
            persistence.saveTypeInstances(new String[] {"users", username, "password"}, new TypeInstances(new TypeInstance("changeme")));
        } catch (Throwable t) {
            throw new HousemateCommsException("Failed to reset password. Couldn't save new one", t);
        }
    }

    @Override
    public Boolean checkCredentials(String username, String password) {
        String hashed = hash(password);
        try {
            TypeInstances typeInstances = persistence.getTypeInstances(new String[] {"users", username, "password"});
            String firstValue = typeInstances.getFirstValue();
            return firstValue != null && firstValue.equals(hashed);
        } catch (DetailsNotFoundException e) {
            return false;
        } catch (Throwable t) {
            logger.error("Failed to check credentials", t);
            return false;
        }
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
                new GWTClientEndpoint(ContextListener.INJECTOR.getInstance(new Key<Router<?>>() {})));
    }

    private void removeEndpoint() {
        this.getThreadLocalRequest().getSession().removeAttribute(ATT_NAME);
    }

    private String hash(String input) {
        return input;
    }
}
