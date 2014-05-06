package com.intuso.housemate.web.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.web.client.NotConnectedException;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.server.ContextListener;
import com.intuso.housemate.web.server.GWTClientEndpoint;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class CommsServiceImpl extends RemoteServiceServlet implements CommsService {

    private final static String ATT_NAME = "endpoint";

    private final Log log;
    private final Persistence persistence;

    @Inject
    public CommsServiceImpl() {
        this.log = ContextListener.INJECTOR.getInstance(Log.class);
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
    public void sendMessageToServer(Message<Message.Payload> message) throws NotConnectedException {
        getEndpoint().sendMessage(message);
    }

    public void resetPassword(String username) {
        try {
            persistence.saveTypeInstances(new String[] {"users", username, "password"}, new TypeInstances(new TypeInstance("changeme")));
        } catch (HousemateException e) {
            throw new HousemateRuntimeException("Failed to reset password. Couldn't save new one", e);
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
        } catch (HousemateException e) {
            log.e("Failed to check credentials", e);
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
                new GWTClientEndpoint(
                        ContextListener.INJECTOR.getInstance(Router.class)));
    }

    private void removeEndpoint() {
        this.getThreadLocalRequest().getSession().removeAttribute(ATT_NAME);
    }

    private String hash(String input) {
        return input;
    }
}
