package com.intuso.housemate.server.comms;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.ClientType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealApplication;
import com.intuso.housemate.object.real.RealApplicationInstance;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.server.client.ClientInstance;
import com.intuso.housemate.object.server.client.RemoteClient;
import com.intuso.housemate.object.server.client.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Local representation of a remote client. Internal objects should use this to send messages to the client
 */
public class RemoteClientImpl implements RemoteClient {

    private final Log log;
    private final ClientInstance clientInstance;
    private final Root<?> root;
    private final MainRouter comms;
    private final BiMap<String, RemoteClientImpl> children = HashBiMap.create();
    private final Listeners<RemoteClientListener> listeners = new Listeners<RemoteClientListener>(new CopyOnWriteArrayList<RemoteClientListener>());
    private RemoteClientImpl parent;
    private List<String> route = null;
    private List<Message<?>> messageQueue = new CopyOnWriteArrayList<Message<?>>();
    private boolean applicationInstanceAllowed = false;

    private final StatusListener statusListener = new StatusListener();
    private RealValue<ApplicationStatus> applicationStatus;
    private RealValue<ApplicationInstanceStatus> applicationInstanceStatus;

    public RemoteClientImpl(Log log, ClientInstance clientInstance, Root<?> root, MainRouter comms) {
        this.log= log;
        this.clientInstance = clientInstance;
        this.root = root;
        this.comms = comms;
    }

    private void setParent(RemoteClientImpl parent) {
        this.parent = parent;
    }

    public Root<?> getRoot() {
        return root;
    }

    @Override
    public ClientInstance getClientInstance() {
        return clientInstance;
    }

    @Override
    public void sendMessage(String[] path, String type, Message.Payload payload) throws HousemateException {
        if(!applicationInstanceAllowed
                && !(path.length == 1
                    && (type.equals(Root.SERVER_CONNECTION_STATUS_TYPE)
                        || type.equals(Root.APPLICATION_STATUS_TYPE)
                        || type.equals(Root.APPLICATION_INSTANCE_STATUS_TYPE)
                        || type.equals(Root.SERVER_INSTANCE_ID_TYPE)
                        || type.equals(Root.APPLICATION_INSTANCE_ID_TYPE))))
            throw new HousemateException("Remote client is not allowed access");
        else if(route == null)
            messageQueue.add(new Message<Message.Payload>(path, type, payload));
        else {
            try {
                comms.sendMessageToClient(path, type, payload, this);
            } catch(HousemateException e) {
                messageQueue.add(new Message<Message.Payload>(path, type, payload));
            }
        }
    }

    private void updateStatus() {
        ApplicationInstanceStatus applicationInstanceStatus = this.applicationInstanceStatus.getTypedValue();
        ApplicationStatus applicationStatus = this.applicationStatus.getTypedValue();
        try {
            sendMessage(new String[]{""}, Root.APPLICATION_STATUS_TYPE, applicationStatus);
            sendMessage(new String[]{""}, Root.APPLICATION_INSTANCE_STATUS_TYPE, applicationInstanceStatus);
        } catch(Throwable t) {
            log.e("Failed to send message to client", t);
        }
        this.applicationInstanceAllowed = applicationInstanceStatus == ApplicationInstanceStatus.Allowed;
        for(RemoteClientListener listener : listeners)
            listener.statusChanged(applicationStatus, applicationInstanceStatus);
        while(messageQueue.size() > 0) {
            try {
                Message message = messageQueue.get(0);
                comms.sendMessageToClient(message.getPath(), message.getType(), message.getPayload(), this);
                messageQueue.remove(0);
            } catch(HousemateException e) {
                break;
            }
        }
    }

    @Override
    public ListenerRegistration addListener(RemoteClientListener listener) {
        return listeners.addListener(listener);
    }

    public RemoteClientImpl addClient(List<String> route, Root<?> root, ClientInstance clientInstance)
                throws HousemateException {
        RemoteClientImpl client = new RemoteClientImpl(log, clientInstance, root, comms);
        client.setBaseRoute(route);
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

    public void unregister() {
        for(RemoteClientListener listener : listeners)
            listener.unregistered(this);
    }

    public Set<RemoteClientImpl> getChildren() {
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
        for(Map.Entry<String, RemoteClientImpl> entry : children.entrySet()) {
            List<String> childRoute = Lists.newArrayList(route);
            childRoute.add(entry.getKey());
            entry.getValue().setBaseRoute(childRoute);
        }
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

        // else, check there is a client with access for the next key
        } else {
            if(!current.children.containsKey(client.getRoute().get(currentIndex)))
                throw new HousemateException("No client with access at index " + currentIndex + " of route " + Message.routeToString(client.getRoute()));
            else if(current.children.get(client.getRoute().get(currentIndex)).getClientInstance().getClientType() != ClientType.Router)
                throw new HousemateException("Client at index " + currentIndex + " of route " + Message.routeToString(client.getRoute()) + " is not of type " + ClientType.Router.name());
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

    public String getRouter(List<String> route) {
        return getRouter(this, 0, route);
    }

    private static String getRouter(RemoteClientImpl current, int currentIndex, List<String> route) {

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

    @Override
    public void setApplicationAndInstance(RealApplication application, RealApplicationInstance applicationInstance) {
        applicationStatus = application.getStatusValue();
        applicationInstanceStatus = applicationInstance.getStatusValue();
        applicationStatus.addObjectListener(statusListener);
        applicationInstanceStatus.addObjectListener(statusListener);
        updateStatus();
    }

    private class StatusListener implements ValueListener<RealValue<?>> {

        @Override
        public void valueChanging(RealValue<?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(RealValue<?> value) {
            updateStatus();
        }
    }
}
