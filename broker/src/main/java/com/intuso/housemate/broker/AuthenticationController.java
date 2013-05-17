package com.intuso.housemate.broker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.broker.client.LocalClient;
import com.intuso.housemate.broker.client.RemoteClient;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.real.BrokerRealUser;
import com.intuso.housemate.broker.storage.DetailsNotFoundException;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.authentication.AuthenticationMethod;
import com.intuso.housemate.core.authentication.Session;
import com.intuso.housemate.core.authentication.UsernamePassword;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.object.connection.ClientWrappable;
import com.intuso.housemate.core.object.root.Root;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 13/05/13
 * Time: 08:36
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationController {

    private final BrokerGeneralResources resources;
    private final RemoteClient root = new RemoteClient(UUID.randomUUID().toString(), ClientWrappable.Type.ROUTER);
    private final Map<String, List<String>> clients = Maps.newHashMap();

    public AuthenticationController(BrokerGeneralResources resources) {
        this.resources = resources;
        clients.put(root.getConnectionId(), Lists.<String>newArrayList());
    }

    public void processRequest(Root.AuthenticationRequest request, List<String> route) {

        // create a new connection id
        String connectionId = UUID.randomUUID().toString();
        RemoteClient client;

        // try and add the client. If this fails, it's because one of the intermediate clients isn't authenticated
        // in which case it shouldn't have allowed this message through. We shouldn't reply to prevent misuse
        try {
            client = addClient(connectionId, route, request.getType());
        } catch(HousemateException e) {
            resources.getLog().e("Failed to add client endpoint for " + Message.routeToString(route));
            root.removeClient(route);
            return;
        }

        // client is added, now authenticate it
        Root.AuthenticationResponse response;
        try {
            response = checkMethod(request.getMethod(), connectionId);
        } catch(Throwable t) {
            response = new Root.AuthenticationResponse("Could not check authentication: " + t.getMessage());
        }

        try {
            if(response != null)
                resources.getComms().sendMessageToClient(new String[] {""}, Root.AUTHENTICATION_RESPONSE, response, client);
        } catch(HousemateException e) {
            resources.getLog().e("Failed to send authentication response to client at route " + Message.routeToString(route));
            response = null;
        }

        // if response is null, or it has no user id and is not one of the internal methods, then remove the client
        if(response == null
                || (response.getUserId() == null && !(request.getMethod() instanceof LocalClient.InternalConnectMethod)))
            removeClient(client);
    }

    private Root.AuthenticationResponse checkMethod(AuthenticationMethod method, String connectionId) throws HousemateException {
        // check by session
        if(method instanceof Session) {
            Session s = (Session)method;
            BrokerRealUser user = getUserForSession(s.getSessionId());
            if(user != null) {
                return new Root.AuthenticationResponse(s.getSessionId(), user.getId());
            } else {
                return new Root.AuthenticationResponse("Unknown Session");
            }

            // check username/passwords match
        } else if(method instanceof UsernamePassword) {
            UsernamePassword up = (UsernamePassword)method;
            BrokerRealUser user = authenticateUser(up.getUsername(), up.getPassword());
            if(user != null) {
                saveUserSession(connectionId, user);
                return new Root.AuthenticationResponse(connectionId, user.getId());
            } else {
                return new Root.AuthenticationResponse("Incorrect credentials");
            }

            // anything internal should just be accepted regardless
        } else if(method instanceof LocalClient.InternalConnectMethod) {
            return new Root.AuthenticationResponse(connectionId, null);

            // unknown method
        } else {
            if(method != null)
                return new Root.AuthenticationResponse("Unknown method used for authentication request: " + method.getClass().getName());
            else
                return new Root.AuthenticationResponse("No method used for authentication request");
        }
    }

    private RemoteClient addClient(String connectionId, List<String> route, ClientWrappable.Type type) throws HousemateException {
        RemoteClient client = root.addClient(route, connectionId, type);
        clients.put(connectionId, route);
        return client;
    }

    public void removeClient(List<String> route) {
        RemoteClient client = root.removeClient(route);
        if(client != null)
            clients.remove(client.getConnectionId());
    }

    public void removeClient(RemoteClient client) {
        if(client != null)
            root.removeClient(clients.remove(client.getConnectionId()));
    }

    private BrokerRealUser getUserForSession(String sessionId) throws HousemateException {
        try {
            Map<String, String> details = resources.getStorage().getDetails(new String[] {"sessions"}, sessionId);
            String userId = details.get("user-id");
            return resources.getRealResources().getRoot().getUsers().get(userId);
        } catch(DetailsNotFoundException e) {
            return null;
        }
    }

    private void saveUserSession(String sessionId, BrokerRealUser user) throws HousemateException {
        Map<String, String> details = Maps.newHashMap();
        details.put("user-id", user.getId());
        resources.getStorage().saveDetails(new String[] {"sessions"}, sessionId, details);
    }

    private BrokerRealUser authenticateUser(String username, String password) throws HousemateException {
        // todo hash the password
        String passwordHash = password;
        // todo look up the id of the user with that username
        String userId = username;
        try {
            Map<String, String> details = resources.getStorage().getDetails(resources.getRealResources().getRoot().getUsers().getPath(), userId);
            String storedPasswordHash = details.get("password-hash");
            if(storedPasswordHash != null && storedPasswordHash.equals(passwordHash))
                return resources.getRealResources().getRoot().getUsers().get(userId);
            else
                return null;
        } catch(DetailsNotFoundException e) {
            return null;
        }
    }

    public List<String> getClientRoute(String clientId) {
        return clients.get(clientId);
    }

    public RemoteClient getClient(List<String> route) {
        return root.getChild(route);
    }
}
