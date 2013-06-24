package com.intuso.housemate.broker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.Reconnect;
import com.intuso.housemate.api.authentication.Session;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.message.ReconnectResponse;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.client.LocalClient;
import com.intuso.housemate.broker.comms.RemoteClientImpl;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.storage.DetailsNotFoundException;
import com.intuso.housemate.object.broker.real.BrokerRealUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class RemoteClientManager {

    private final static String BROKER_INSTANCE_ID = UUID.randomUUID().toString();

    private final BrokerGeneralResources resources;
    private final RemoteClientImpl root;
    private final Map<String, RemoteClientImpl> lostClients = Maps.newHashMap();

    public RemoteClientManager(BrokerGeneralResources resources) {
        this.resources = resources;
        root = new RemoteClientImpl(UUID.randomUUID().toString(), ConnectionType.Router, resources.getMainRouter());
        root.setRoute(Lists.<String>newArrayList());
    }

    public void processRequest(AuthenticationRequest request, List<String> route) {
        resources.getLog().d("Authentication request received");
        if(!(request.getMethod() instanceof Reconnect) || !processReconnect(request, route))
            processNewClient(request, route);
    }

    private boolean processReconnect(AuthenticationRequest request, List<String> route) {
        resources.getLog().d("Reconnect request received");
        RemoteClientImpl client = lostClients.remove(((Reconnect) request.getMethod()).getConnectionId());
        if(client == null) {
            resources.getLog().w("Could not find client for connection id " + ((Reconnect)request.getMethod()).getConnectionId());
            return false;
        }
        client.setRoute(route);
        try {
            root.addClient(client);
        } catch(HousemateException e) {
            resources.getLog().e("Failed to add reconnected client");
            return false;
        }
        try {
            resources.getLog().d("Reconnect succeeded");
            client.sendMessage(new String[] {""}, Root.CONNECTION_RESPONSE_TYPE, new ReconnectResponse());
        } catch(HousemateException e) {
            resources.getLog().e("Failed to send authentication response to client at route " + Message.routeToString(route));
            return false;
        }
        return true;
    }

    private void processNewClient(AuthenticationRequest request, List<String> route) {

        resources.getLog().d("Handling new client request");

        // create a new connection id
        String connectionId = UUID.randomUUID().toString();
        RemoteClientImpl client;

        // try and add the client. If this fails, it's because one of the intermediate clients isn't authenticated
        // in which case it shouldn't have allowed this message through. We shouldn't reply to prevent misuse
        try {
            client = addClient(connectionId, route, request.getType());
            client.setRoute(route);
        } catch(HousemateException e) {
            resources.getLog().e("Failed to add client endpoint for " + Message.routeToString(route));
            resources.getLog().d("Maybe one of the intermediate clients isn't connected or isn't of type " + ConnectionType.Router);
            return;
        }

        // client is added, now authenticate it
        AuthenticationResponse response;
        try {
            response = checkMethod(request.getMethod(), connectionId);
        } catch(Throwable t) {
            response = new AuthenticationResponse(BROKER_INSTANCE_ID, "Could not check authentication: " + t.getMessage());
        }

        try {
            if(response != null) {
                resources.getLog().d("Authentication succeeded");
                client.sendMessage(new String[]{""}, Root.CONNECTION_RESPONSE_TYPE, response);
            }
        } catch(HousemateException e) {
            resources.getLog().e("Failed to send authentication response to client at route " + Message.routeToString(route));
            response = null;
        }

        // if response is null, or it has no user id and is not one of the internal methods, then remove the client
        if(response == null
                || (response.getUserId() == null && !(request.getMethod() instanceof LocalClient.InternalConnectMethod))) {
            if(response == null)
                resources.getLog().d("Authentication failed. See previous log messages for details");
            else
                resources.getLog().d("Authentication failed: " + response.getProblem());
            removeClient(client);
        }
    }

    private AuthenticationResponse checkMethod(AuthenticationMethod method, String connectionId) throws HousemateException {
        // check by session
        if(method == null) {
            resources.getLog().d("No authentication method");
            return new AuthenticationResponse(BROKER_INSTANCE_ID, "No method used for authentication request");
        } else if(method instanceof Session) {
            Session s = (Session)method;
            resources.getLog().d("Authentication by session id " + s.getSessionId());
            BrokerRealUser user = getUserForSession(s.getSessionId());
            if(user != null) {
                return new AuthenticationResponse(BROKER_INSTANCE_ID, s.getSessionId(), user.getId());
            } else {
                return new AuthenticationResponse(BROKER_INSTANCE_ID, "Unknown Session");
            }

            // check username/passwords match
        } else if(method instanceof UsernamePassword) {
            UsernamePassword up = (UsernamePassword)method;
            resources.getLog().d("Authentication by username/password " + up.getUsername());
            BrokerRealUser user = authenticateUser(up.getUsername(), up.getPassword());
            if(user != null) {
                saveUserSession(connectionId, user);
                return new AuthenticationResponse(BROKER_INSTANCE_ID, connectionId, user.getId());
            } else {
                return new AuthenticationResponse(BROKER_INSTANCE_ID, "Incorrect credentials");
            }

            // anything internal should just be accepted regardless
        } else if(method instanceof LocalClient.InternalConnectMethod) {
            resources.getLog().d("Internal client authentication");
            return new AuthenticationResponse(BROKER_INSTANCE_ID, connectionId, null);

            // reconnect
        } else if(method instanceof Reconnect) {
            return new AuthenticationResponse(BROKER_INSTANCE_ID, "Unknown connection id");

            // unknown method
        } else {
            resources.getLog().d("Unknown authentication method: " + method.getClass().getName());
            return new AuthenticationResponse(BROKER_INSTANCE_ID, "Unknown method used for authentication request: " + method.getClass().getName());
        }
    }

    private RemoteClientImpl addClient(String connectionId, List<String> route, ConnectionType type) throws HousemateException {
        RemoteClientImpl client = root.addClient(route, connectionId, type);
        return client;
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

    private BrokerRealUser getUserForSession(String sessionId) throws HousemateException {
        try {
            TypeInstances details = resources.getStorage().getValues(new String[]{"sessions"}, sessionId);
            String userId = details.get("user-id").getValue();
            return resources.getRealResources().getRoot().getUsers().get(userId);
        } catch(DetailsNotFoundException e) {
            return null;
        }
    }

    private void saveUserSession(String sessionId, BrokerRealUser user) throws HousemateException {
        TypeInstances details = new TypeInstances();
        details.put("user-id", new TypeInstance(user.getId()));
        resources.getStorage().saveValues(new String[]{"sessions"}, sessionId, details);
    }

    private BrokerRealUser authenticateUser(String username, String password) throws HousemateException {
        String passwordHash;
        try {
            passwordHash = new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
        } catch(NoSuchAlgorithmException e) {
            throw new HousemateException("Unable to hash password to compare with saved hashed version", e);
        }
        // todo look up the id of the user with that username
        String userId = username;
        try {
            TypeInstances details = resources.getStorage().getValues(resources.getRealResources().getRoot().getUsers().getPath(), userId);
            TypeInstance storedPasswordHash = details.get("password-hash");
            if(storedPasswordHash != null
                    && storedPasswordHash.getValue() != null
                    && storedPasswordHash.getValue().equals(passwordHash))
                return resources.getRealResources().getRoot().getUsers().get(userId);
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
