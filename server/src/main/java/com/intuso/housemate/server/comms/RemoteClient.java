package com.intuso.housemate.server.comms;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.payload.ApplicationData;
import com.intuso.housemate.comms.api.internal.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.api.internal.payload.RootData;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Local representation of a remote client. Internal objects should use this to send messages to the client
 */
public class RemoteClient {

    private final Log log;
    private final ClientInstance clientInstance;
    private final Message.Receiver<Message.Payload> receiver;
    private final MainRouter comms;
    private final BiMap<String, RemoteClient> children = HashBiMap.create();
    private final Listeners<RemoteClientListener> listeners = new Listeners<>(new CopyOnWriteArrayList<RemoteClientListener>());
    private RemoteClient parent;
    private List<String> route = null;
    private List<Message<?>> messageQueue = new CopyOnWriteArrayList<>();
    private boolean applicationInstanceAllowed = false;

    public RemoteClient(Log log, ClientInstance clientInstance, Message.Receiver<Message.Payload> receiver, MainRouter comms) {
        this.log= log;
        this.clientInstance = clientInstance;
        this.receiver = receiver;
        this.comms = comms;
    }

    private void setParent(RemoteClient parent) {
        this.parent = parent;
    }

    public Message.Receiver<Message.Payload> getReceiver() {
        return receiver;
    }

    public ClientInstance getClientInstance() {
        return clientInstance;
    }

    public void sendMessage(String[] path, String type, Message.Payload payload) {
        if(!applicationInstanceAllowed
                && !(path.length == 1
                    && (type.equals(RootData.SERVER_CONNECTION_STATUS_TYPE)
                        || type.equals(RootData.APPLICATION_STATUS_TYPE)
                        || type.equals(RootData.APPLICATION_INSTANCE_STATUS_TYPE)
                        || type.equals(RootData.SERVER_INSTANCE_ID_TYPE)
                        || type.equals(RootData.APPLICATION_INSTANCE_ID_TYPE))))
            throw new HousemateCommsException("Remote client is not allowed access");
        else if(route == null)
            messageQueue.add(new Message<>(path, type, payload));
        else {
            try {
                comms.sendMessageToClient(path, type, payload, this);
            } catch(Throwable e) {
                messageQueue.add(new Message<>(path, type, payload));
            }
        }
    }

    public void setApplicationAndInstanceStatus(Application.Status applicationStatus, ApplicationInstance.Status applicationInstanceStatus) {

        try {
            sendMessage(new String[]{""}, RootData.APPLICATION_STATUS_TYPE, new ApplicationData.StatusPayload(applicationStatus));
            sendMessage(new String[]{""}, RootData.APPLICATION_INSTANCE_STATUS_TYPE, new ApplicationInstanceData.StatusPayload(applicationInstanceStatus));
        } catch(Throwable t) {
            log.e("Failed to send message to client", t);
        }
        this.applicationInstanceAllowed = applicationInstanceStatus == ApplicationInstance.Status.Allowed;
        for(RemoteClientListener listener : listeners)
            listener.statusChanged(applicationStatus, applicationInstanceStatus);
        while(messageQueue.size() > 0) {
            try {
                Message message = messageQueue.get(0);
                comms.sendMessageToClient(message.getPath(), message.getType(), message.getPayload(), this);
                messageQueue.remove(0);
            } catch(Throwable t) {
                break;
            }
        }
    }

    public ListenerRegistration addListener(RemoteClientListener listener) {
        return listeners.addListener(listener);
    }

    public RemoteClient addClient(List<String> route, Message.Receiver<Message.Payload> receiver, ClientInstance clientInstance) {
        RemoteClient client = new RemoteClient(log, clientInstance, receiver, comms);
        client.setBaseRoute(route);
        addClient(client);
        return client;
    }

    public void addClient(RemoteClient remoteClient) {
        addClient(parent, this, 0, remoteClient);
    }

    public RemoteClient getClient(List<String> route) {
        return getClient(this, 0, route);
    }

    public void remove() {
        if(parent != null)
            parent.children.inverse().remove(this);
        parent = null;
    }

    public void unregister() {
        for(RemoteClientListener listener : listeners)
            listener.unregistered(this);
    }

    public Set<RemoteClient> getChildren() {
        return children.values();
    }

    public void connectionLost() {
        route = null;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setBaseRoute(List<String> route) {
        this.route = route;
        for(Map.Entry<String, RemoteClient> entry : children.entrySet()) {
            List<String> childRoute = Lists.newArrayList(route);
            childRoute.add(entry.getKey());
            entry.getValue().setBaseRoute(childRoute);
        }
    }

    private static void addClient(RemoteClient parent, RemoteClient current, int currentIndex, RemoteClient client) {

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
                throw new HousemateCommsException("Client route already exists");
            }
            current.children.put(client.getRoute().get(currentIndex), client);
            client.setParent(current);
            return;

        // else, check there is a client with access for the next key
        } else {
            if(!current.children.containsKey(client.getRoute().get(currentIndex)))
                throw new HousemateCommsException("No client with access at index " + currentIndex + " of route " + Arrays.toString(client.getRoute().toArray()));
            else if(current.children.get(client.getRoute().get(currentIndex)).getClientInstance().getClientType() != ApplicationRegistration.ClientType.Router)
                throw new HousemateCommsException("Client at index " + currentIndex + " of route " + Arrays.toString(client.getRoute().toArray()) + " is not of type " + ApplicationRegistration.ClientType.Router.name());
            addClient(current, current.children.get(client.getRoute().get(currentIndex)), currentIndex + 1, client);
        }
    }

    private static RemoteClient getClient(RemoteClient current, int currentIndex, List<String> route) {

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

    public String getRouter(List<String> route) {
        return getRouter(this, 0, route);
    }

    private static String getRouter(RemoteClient current, int currentIndex, List<String> route) {

        // if at or past the end, return null
        if(route.size() <= currentIndex)
            return null;

        else if(current.children.containsKey(route.get(currentIndex)))
            return getRouter(current.children.get(route.get(currentIndex)), currentIndex + 1, route);

        else
            return null;
    }

    @Override
    public int hashCode() {
        return clientInstance.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoteClient && clientInstance.equals(((RemoteClient)o).getClientInstance());
    }

    public boolean isApplicationInstanceAllowed() {
        return applicationInstanceAllowed;
    }
}
