package com.intuso.housemate.object.broker;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.connection.ClientWrappable;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 13/05/13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class RemoteClient {

    private final String connectionId;
    private final ClientWrappable.Type type;
    private final Map<String, RemoteClient> children = Maps.newHashMap();
    private final Listeners<DisconnectListener> disconnectListeners = new Listeners<DisconnectListener>();

    public RemoteClient(String connectionId, ClientWrappable.Type type) {
        this.connectionId = connectionId;
        this.type = type;
    }

    public ListenerRegistration<DisconnectListener> addDisconnectListener(DisconnectListener listener) {
        return disconnectListeners.addListener(listener);
    }

    public String getConnectionId() {
        return connectionId;
    }

    public ClientWrappable.Type getType() {
        return type;
    }

    public RemoteClient getChild(List<String> route) {
        return getChild(this, 0, route);
    }

    public RemoteClient addClient(List<String> route, String connectionId, ClientWrappable.Type type) throws HousemateException {
        return addClient(this, 0, route, connectionId, type);
    }

    public RemoteClient removeClient(List<String> route) {
        return removeClient(this, 0, route);
    }

    private void removed() {
        for(DisconnectListener listener : disconnectListeners)
            listener.disconnected(this);
        for(RemoteClient child : children.values())
            child.removed();
    }

    private static RemoteClient getChild(RemoteClient current, int currentIndex, List<String> route) {

        // if we're past the end, then return
        if(route.size() <= currentIndex)
            return current;

        // else if the next key exists then recurse
        else if(current.children.containsKey(route.get(currentIndex)))
            return getChild(current.children.get(route.get(currentIndex)), currentIndex + 1, route);

        // otherwise return null
        else
            return null;
    }

    private static RemoteClient addClient(RemoteClient current, int currentIndex, List<String> route, String connectionId,
                                 ClientWrappable.Type type) throws HousemateException {

        // if we're past the end, then just return
        if(route.size() <= currentIndex)
            return current;

        // else, if we're at the last position, then check the client route doesn't already exist
        else if(route.size() - 1 == currentIndex) {
            if(current.children.containsKey(route.get(currentIndex)))
                throw new HousemateException("Client route already exists");
            RemoteClient client = new RemoteClient(connectionId, type);
            current.children.put(route.get(currentIndex), client);
            return client;

        // else, check there is an authorised client for the next key
        } else {
            if(!current.children.containsKey(route.get(currentIndex)))
                throw new HousemateException("No authorised client at index " + currentIndex + " of route " + Message.routeToString(route));
            else if(current.children.get(route.get(currentIndex)).getType() != ClientWrappable.Type.ROUTER)
                throw new HousemateException("Client at index " + currentIndex + " of route " + Message.routeToString(route) + " is not of type " + ClientWrappable.Type.ROUTER.name());
            return addClient(current.children.get(route.get(currentIndex)), currentIndex + 1, route, connectionId, type);
        }
    }

    private static RemoteClient removeClient(RemoteClient current, int currentIndex, List<String> route) {

        // if we're past the end then just return
        if(route.size() <= currentIndex)
            return null;

        // else if the next key exists then recurse
        else if(current.children.containsKey(route.get(currentIndex))) {
            RemoteClient result = removeClient(current.children.get(route.get(currentIndex)), currentIndex + 1, route);

            // if we're at the end then remove it
            if(route.size() - 1 == currentIndex) {
                // todo also handle whatever children the just-deleted client has
                result = current.children.remove(route.get(currentIndex));
                if(result != null)
                    result.removed();
            }

            return result;
        } else
            return null;
    }
}
