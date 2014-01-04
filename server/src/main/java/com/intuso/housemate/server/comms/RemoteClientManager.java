package com.intuso.housemate.server.comms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.*;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.message.ReconnectResponse;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealUser;
import com.intuso.housemate.server.storage.DetailsNotFoundException;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class RemoteClientManager {

    private final static String SERVER_INSTANCE_ID = UUID.randomUUID().toString();

    private final Log log;
    private final ServerRealResources serverRealResources;
    private final Storage storage;
    
    private final RemoteClientImpl root;
    private final Map<String, RemoteClientImpl> lostClients = Maps.newHashMap();

    @Inject
    public RemoteClientManager(Log log, ServerRealResources serverRealResources, Storage storage, MainRouter mainRouter) {
        this.log = log;
        this.serverRealResources = serverRealResources;
        this.storage = storage;
        root = new RemoteClientImpl(UUID.randomUUID().toString(), null, ConnectionType.Router, mainRouter, false);
        root.setRoute(Lists.<String>newArrayList());
    }

    public void processRequest(AuthenticationRequest request, List<String> route) {
        log.d("Authentication request received");
        if(!(request.getMethod() instanceof Reconnect) || !processReconnect(request, route))
            processNewClient(request, route);
    }

    private boolean processReconnect(AuthenticationRequest request, List<String> route) {
        log.d("Reconnect request received");
        RemoteClientImpl client = lostClients.remove(((Reconnect) request.getMethod()).getConnectionId());
        if(client == null) {
            log.w("Could not find client for connection id " + ((Reconnect)request.getMethod()).getConnectionId());
            return false;
        }
        client.setRoute(route);
        try {
            root.addClient(client);
        } catch(HousemateException e) {
            log.e("Failed to add reconnected client");
            return false;
        }
        try {
            log.d("Reconnect succeeded");
            client.sendMessage(new String[] {""}, Root.CONNECTION_RESPONSE_TYPE, new ReconnectResponse());
        } catch(HousemateException e) {
            log.e("Failed to send authentication response to client at route " + Message.routeToString(route));
            return false;
        }
        return true;
    }

    private void processNewClient(AuthenticationRequest request, List<String> route) {

        log.d("Handling new client request");

        // create a new connection id
        String connectionId = UUID.randomUUID().toString();
        RemoteClientImpl client;

        // check authentication method
        AuthenticationResponse response;
        try {
            response = checkMethod(route, request.getMethod(), connectionId);
        } catch(Throwable t) {
            response = new AuthenticationResponse(SERVER_INSTANCE_ID, "Could not check authentication: " + t.getMessage());
        }

        // try and add the client. If this fails, it's because one of the intermediate clients isn't authenticated
        // in which case it shouldn't have allowed this message through. We shouldn't reply to prevent misuse
        try {
            client = addClient(connectionId, response.getUserId(), route, request.getType(), request.getType() == ConnectionType.Router && request.getMethod().isClientsAuthenticated());
            client.setRoute(route);
        } catch(HousemateException e) {
            log.e("Failed to add client endpoint for " + Message.routeToString(route), e);
            log.d("Maybe one of the intermediate clients isn't connected or isn't of type " + ConnectionType.Router);
            return;
        }

        try {
            if(response != null) {
                log.d("Authentication succeeded");
                client.sendMessage(new String[]{""}, Root.CONNECTION_RESPONSE_TYPE, response);
            }
        } catch(HousemateException e) {
            log.e("Failed to send authentication response to client at route " + Message.routeToString(route));
            response = null;
        }

        // if response is null, or it has no user id and is not one of the internal methods, then remove the client
        if(response == null ||
                (response.getUserId() == null && !(request.getMethod() instanceof InternalAuthentication))) {
            if(response == null)
                log.d("Authentication failed. See previous log messages for details");
            else
                log.d("Authentication failed: " + response.getProblem());
            removeClient(client);
        }
    }

    private AuthenticationResponse checkMethod(List<String> route, AuthenticationMethod method, String connectionId) throws HousemateException {
        // check by session
        if(method == null) {
            log.d("No authentication method");
            return new AuthenticationResponse(SERVER_INSTANCE_ID, "No method used for authentication request");
        } else if(method instanceof Session) {
            Session s = (Session)method;
            log.d("Authentication by session id " + s.getSessionId());
            ServerRealUser user = getUserForSession(s.getSessionId());
            if(user != null) {
                return new AuthenticationResponse(SERVER_INSTANCE_ID, s.getSessionId(), user.getId());
            } else {
                return new AuthenticationResponse(SERVER_INSTANCE_ID, "Unknown Session");
            }

            // check username/passwords match
        } else if(method instanceof UsernamePassword) {
            UsernamePassword up = (UsernamePassword)method;
            log.d("Authentication by username/password " + up.getUsername());
            ServerRealUser user = authenticateUser(up.getUsername(), up.getPassword());
            if(user != null) {
                saveUserSession(connectionId, user);
                return new AuthenticationResponse(SERVER_INSTANCE_ID, connectionId, user.getId());
            } else {
                return new AuthenticationResponse(SERVER_INSTANCE_ID, "Incorrect credentials");
            }

            // need to check that an intermediate router has authed its clients
        } else if(method instanceof FromRouter) {
            String user = root.getAuthenticatedRouter(route);
            if(user != null)
                return new AuthenticationResponse(SERVER_INSTANCE_ID, connectionId, user);
            else
                return new AuthenticationResponse(SERVER_INSTANCE_ID, "No intermediate router has authenticated for clients");

            // anything internal should just be accepted regardless
        } else if(method instanceof InternalAuthentication) {
            log.d("Internal client authentication");
            return new AuthenticationResponse(SERVER_INSTANCE_ID, connectionId, null);

            // reconnect
        } else if(method instanceof Reconnect) {
            return new AuthenticationResponse(SERVER_INSTANCE_ID, "Unknown connection id");

            // unknown method
        } else {
            log.d("Unknown authentication method: " + method.getClass().getName());
            return new AuthenticationResponse(SERVER_INSTANCE_ID, "Unknown method used for authentication request: " + method.getClass().getName());
        }
    }

    private RemoteClientImpl addClient(String connectionId, String authenticatedUser, List<String> route,
                                       ConnectionType type, boolean clientsAuthenticated) throws HousemateException {
        return root.addClient(route, connectionId, authenticatedUser, type, clientsAuthenticated);
    }

    public void clientDisconnected(List<String> route) {
        RemoteClientImpl client = root.getClient(route);
        if(client != null) {
            client.remove();
            clientDisconnected(client);
        }
    }

    private void clientDisconnected(RemoteClientImpl client) {
        client.disconnected();
        for(RemoteClientImpl child : client.getChildren())
            clientDisconnected(child);
    }

    public void connectionLost(List<String> route) {
        RemoteClientImpl client = root.getClient(route);
        if(client != null)
            connectionLost(client);
    }

    private void connectionLost(RemoteClientImpl client) {
        client.remove();
        client.connectionLost();
        lostClients.put(client.getConnectionId(), client);
        for(RemoteClientImpl child : client.getChildren())
            connectionLost(child);
    }

    public void removeClient(RemoteClientImpl client) {
        if(client != null)
            root.getClient(client.getRoute()).remove();
    }

    private ServerRealUser getUserForSession(String sessionId) throws HousemateException {
        try {
            TypeInstanceMap details = storage.getValues(new String[]{"sessions"}, sessionId);
            String userId = details.get("user-id").getFirstValue();
            return serverRealResources.getRoot().getUsers().get(userId);
        } catch(DetailsNotFoundException e) {
            return null;
        }
    }

    private void saveUserSession(String sessionId, ServerRealUser user) throws HousemateException {
        TypeInstanceMap details = new TypeInstanceMap();
        details.put("user-id", new TypeInstances(new TypeInstance(user.getId())));
        storage.saveValues(new String[]{"sessions"}, sessionId, details);
    }

    private ServerRealUser authenticateUser(String username, String password) throws HousemateException {
        String passwordHash;
        try {
            passwordHash = new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
        } catch(NoSuchAlgorithmException e) {
            throw new HousemateException("Unable to hash password to compare with saved hashed version", e);
        }
        // todo look up the id of the user with that username
        String userId = username;
        try {
            TypeInstanceMap details = storage.getValues(serverRealResources.getRoot().getUsers().getPath(), userId);
            TypeInstances storedPasswordHash = details.get("password-hash");
            if(storedPasswordHash != null
                    && storedPasswordHash.getFirstValue() != null
                    && storedPasswordHash.getFirstValue().equals(passwordHash))
                return serverRealResources.getRoot().getUsers().get(userId);
            else
                return null;
        } catch(DetailsNotFoundException e) {
            // check there are actually some users
            return null;
        }
    }

    public RemoteClientImpl getClient(List<String> route) {
        return root.getClient(route);
    }
}
