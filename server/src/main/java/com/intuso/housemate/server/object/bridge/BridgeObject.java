package com.intuso.housemate.server.object.bridge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.server.client.ClientPayload;
import com.intuso.housemate.object.server.client.RemoteClient;
import com.intuso.housemate.object.server.client.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public abstract class BridgeObject<DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends BridgeObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends BridgeObject<DATA, CHILD_DATA, CHILD, OBJECT, LISTENER>,
            LISTENER extends com.intuso.housemate.api.object.ObjectListener>
        extends HousemateObject<DATA, CHILD_DATA, CHILD, LISTENER>
        implements RemoteClientListener, ObjectListener<CHILD> {

    private final Set<RemoteClient> loadedByClients = Sets.newHashSet();
    private final Map<RemoteClient, ListenerRegistration> clientListeners= Maps.newHashMap();

    protected BridgeObject(Log log, ListenersFactory listenersFactory, DATA data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CHILD_OVERVIEWS_REQUEST, new Receiver<ClientPayload<NoPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<NoPayload>> message) throws HousemateException {
                RemoteClient client = message.getPayload().getClient();
                if(client.getClientInstance().getClientType() != ClientType.Proxy) {
                    getLog().e("Client requesting an object is not of type " + ClientType.Proxy);
                    sendMessage(CHILD_OVERVIEWS_RESPONSE, new ChildOverviews("Connection type is not " + ClientType.Proxy.name()), client);
                } else
                    sendMessage(CHILD_OVERVIEWS_RESPONSE, new ChildOverviews(getChildOverviewsForClient(client)), client);
            }
        }));
        result.add(addMessageListener(LOAD_REQUEST, new Receiver<ClientPayload<LoadRequest>>() {
            @Override
            public void messageReceived(Message<ClientPayload<LoadRequest>> message) throws HousemateException {
                RemoteClient client = message.getPayload().getClient();
                String loaderName = message.getPayload().getOriginal().getLoaderName();
                TreeLoadInfo loadInfo = message.getPayload().getOriginal().getLoadInfo();
                String childId = loadInfo.getId();
                if(client.getClientInstance().getClientType() != ClientType.Proxy) {
                    getLog().e("Client requesting an object is not of type " + ClientType.Proxy);
                    sendMessage(LOAD_RESPONSE, new LoadResponse(loaderName, new TreeData(childId, null, null, null), "Connection type is not " + ClientType.Proxy.name()), client);
                } else if(childId.equals(HousemateObject.EVERYTHING_RECURSIVE)
                        || childId.equals(HousemateObject.EVERYTHING)) {
                    for(CHILD child : getChildren()) {
                        if(!child.isLoadedBy(client)) {
                            TreeData data = child.getDataForClient(client, loadInfo);
                            sendMessage(LOAD_RESPONSE, new LoadResponse(loaderName, data), client);
                        }
                    }
                    sendMessage(LOAD_RESPONSE, new LoadResponse(loaderName, new TreeData(childId, null, null, null)), client);
                } else {
                    BridgeObject<?, ?, ?, ?, ?> child = getChild(childId);
                    if (child == null) {
                        getLog().w("Load request received from " + Arrays.toString(message.getRoute().toArray(new String[message.getRoute().size()])) + " for non-existant object \"" + childId + "\"");
                        sendMessage(LOAD_RESPONSE, new LoadResponse(loaderName, new TreeData(childId, null, null, null), "Object does not exist or you do not have permission to see it"), client);
                    } else if(!child.isLoadedBy(client)) {
                        TreeData data = child.getDataForClient(client, loadInfo);
                        sendMessage(LOAD_RESPONSE, new LoadResponse(loaderName, data), client);
                    } else
                        sendMessage(LOAD_RESPONSE, new LoadResponse(loaderName, new TreeData(childId, null, null, null)), client);
                }
            }
        }));
        result.add(addChildListener(this));
        return result;
    }

    @Override
    public void statusChanged(ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {
        // do nothing
    }

    @Override
    public void unregistered(RemoteClient client) {
        if(clientListeners.containsKey(client))
            clientListeners.remove(client).removeListener();
        loadedByClients.remove(client);
    }

    @Override
    public void childObjectAdded(String childId, CHILD child) {
        broadcastMessage(CHILD_ADDED, new ChildOverview(child.getId(), child.getName(), child.getDescription()));
    }

    @Override
    public void childObjectRemoved(String childId, CHILD child) {
        broadcastMessage(CHILD_REMOVED, new ChildOverview(child.getId(), child.getName(), child.getDescription()));
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    private List<ChildOverview> getChildOverviewsForClient(RemoteClient client) {
        List<ChildOverview> result = Lists.newArrayList();
        for(CHILD child : getChildren())
            result.add(new ChildOverview(child.getId(), child.getName(), child.getDescription()));
        return result;
    }

    protected TreeData getDataForClient(RemoteClient client, TreeLoadInfo loadInfo) {
        clientListeners.put(client, client.addListener(this));
        loadedByClients.add(client);
        Map<String, TreeData> children = Maps.newHashMap();
        Map<String, ChildOverview> childData = Maps.newHashMap();
        if(loadInfo.getId().equals(HousemateObject.EVERYTHING_RECURSIVE)) {
            for(CHILD child : getChildren()) {
                children.put(child.getId(), child.getDataForClient(client, loadInfo));
                childData.put(child.getId(), new ChildOverview(child.getId(), child.getName(), child.getDescription()));
            }
        } else {
            for(Map.Entry<String, TreeLoadInfo> entry : loadInfo.getChildren().entrySet()) {
                if(entry.getKey().equals(HousemateObject.EVERYTHING_RECURSIVE) ||
                        entry.getKey().equals(HousemateObject.EVERYTHING))
                    for(CHILD child : getChildren())
                        children.put(child.getId(), child.getDataForClient(client, entry.getValue()));
                else if(getChild(entry.getKey()) != null)
                    children.put(entry.getKey(), getChild(entry.getKey()).getDataForClient(client, entry.getValue()));
            }
            for(CHILD child : getChildren())
                childData.put(child.getId(), new ChildOverview(child.getId(), child.getName(), child.getDescription()));
        }
        return new TreeData(getId(), getData().clone(), children, childData);
    }

    protected boolean isLoadedBy(RemoteClient client) {
        return loadedByClients.contains(client);
    }

    protected void clearClientInfo(RemoteClient client) {
        loadedByClients.remove(client);
        for(CHILD child : getChildren())
            child.clearClientInfo(client);
    }

    protected void sendMessage(String type, Message.Payload payload, RemoteClient client) throws HousemateException {
        client.sendMessage(getPath(), type, payload);
    }

    protected <MV extends Message.Payload> void broadcastMessage(String type, MV content) {
        for(RemoteClient client : loadedByClients) {
            try {
                sendMessage(type, content, client);
            } catch(HousemateException e) {
                getLog().e("Failed to broadcast message to client");
                getLog().e(e.getMessage());
            }
        }
    }

    protected void addLoadedBy(BridgeObject<?, ?, ?, ?, ?> element) {
        element.loadedByClients.addAll(loadedByClients);
        for(BridgeObject<?, ?, ?, ?, ?> child : element.getChildren())
            addLoadedBy(child);
    }

    protected final OBJECT getThis() {
        return (OBJECT)this;
    }
}
