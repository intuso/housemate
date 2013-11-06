package com.intuso.housemate.broker.comms;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.housemate.object.broker.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

import java.util.List;
import java.util.Set;

/**
 * Local representation of a remote client. Internal objects should use this to send messages to the client
 */
public class RemoteClientImpl implements RemoteClient {

    private final String connectionId;
    private final String authenticatedUser;
    private final ConnectionType type;
    private final MainRouter comms;
    private final BiMap<String, RemoteClientImpl> children = HashBiMap.create();
    private final Listeners<RemoteClientListener> listeners = new Listeners<RemoteClientListener>();
    private RemoteClientImpl parent;
    private List<String> route = null;
    private final boolean clientsAuthenticated;

    public RemoteClientImpl(String connectionId, String authenticatedUser, ConnectionType type, MainRouter comms,
                            boolean clientsAuthenticated) {
        this.connectionId = connectionId;
        this.authenticatedUser = authenticatedUser;
        this.type = type;
        this.comms = comms;
        this.clientsAuthenticated = clientsAuthenticated;
    }

    private void setParent(RemoteClientImpl parent) {
        this.parent = parent;
    }

    /**
     * Get the id of the connection
     * @return the id of the connection
     */
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public ConnectionType getType() {
        return type;
    }

    @Override
    public void sendMessage(String[] path, String type, Message.Payload payload) throws HousemateException {
        if(route != null)
            comms.sendMessageToClient(path, type, payload, this);
        else
            throw new HousemateException("Remote client is not connected");
    }

    @Override
    public boolean isCurrentlyConnected() {
        return route != null;
    }

    @Override
    public ListenerRegistration addListener(RemoteClientListener listener) {
        return listeners.addListener(listener);
    }

    public RemoteClientImpl addClient(List<String> route, String connectionId, String authenticatedUser,
                                      ConnectionType type, boolean clientsAuthenticated) throws HousemateException {
        RemoteClientImpl client = new RemoteClientImpl(connectionId, authenticatedUser, type, comms,clientsAuthenticated);
        client.setRoute(route);
        addClient(client);
        return client;
    }

    public void addClient(RemoteClientImpl remoteClient) throws HousemateException {
        addClient(parent, this, 0, remoteClient);
    }

    public RemoteClientImpl getClient(List<String> route) {
        return getClient(this, 0, route);
    }

    public void remove() {
        if(parent != null)
            parent.children.inverse().remove(this);
        parent = null;
    }

    public void disconnected() {
        for(RemoteClientListener listener : listeners)
            listener.disconnected(this);
    }

    public Set<RemoteClientImpl> getChildren() {
        return children.values();
    }

    public void connectionLost() {
        route = null;
        for(RemoteClientListener listener : listeners)
            listener.connectionLost(this);
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
        for(RemoteClientListener listener : listeners)
            listener.reconnected(this);
    }

    private static void addClient(RemoteClientImpl parent, RemoteClientImpl current, int currentIndex, RemoteClientImpl client) throws HousemateException {

        // if we're past the end return null
        if(client.getRoute().size() < currentIndex)
            return;

        // if we're at the end return current
        else if(client.getRoute().size() == currentIndex) {
            current.setParent(parent);
            return;
        }

        // else, if we're at the last position, then check the client route doesn't already exist
        else if(client.getRoute().size() - 1 == currentIndex) {
            if(current.children.containsKey(client.getRoute().get(currentIndex))) {
                throw new HousemateException("Client route already exists");
            }
            current.children.put(client.getRoute().get(currentIndex), client);
            client.setParent(current);
            return;

        // else, check there is an authorised client for the next key
        } else {
            if(!current.children.containsKey(client.getRoute().get(currentIndex)))
                throw new HousemateException("No authorised client at index " + currentIndex + " of route " + Message.routeToString(client.getRoute()));
            else if(current.children.get(client.getRoute().get(currentIndex)).getType() != ConnectionType.Router)
                throw new HousemateException("Client at index " + currentIndex + " of route " + Message.routeToString(client.getRoute()) + " is not of type " + ConnectionType.Router.name());
            addClient(current, current.children.get(client.getRoute().get(currentIndex)), currentIndex + 1, client);
        }
    }

    private static RemoteClientImpl getClient(RemoteClientImpl current, int currentIndex, List<String> route) {

        // if we're past the end, return null
        if(route.size() < currentIndex)
            return null;

        // if we're at the end return the current object
        else if(route.size() == currentIndex)
            return current;

        // else if the next key exists then recurse
        else if(current.children.containsKey(route.get(currentIndex)))
            return getClient(current.children.get(route.get(currentIndex)), currentIndex + 1, route);

        else
            return null;
    }

    public String getAuthenticatedRouter(List<String> route) {
        return getAuthenticatedRouter(this, 0, route);
    }

    private static String getAuthenticatedRouter(RemoteClientImpl current, int currentIndex, List<String> route) {

        if(current.clientsAuthenticated)
            return current.authenticatedUser;

        // if at or past the end, return null
        else if(route.size() <= currentIndex)
            return null;

        else if(current.children.containsKey(route.get(currentIndex)))
            return getAuthenticatedRouter(current.children.get(route.get(currentIndex)), currentIndex + 1, route);

        else
            return null;
    }
}
