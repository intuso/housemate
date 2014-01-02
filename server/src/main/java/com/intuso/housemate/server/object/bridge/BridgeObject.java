package com.intuso.housemate.server.object.bridge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.housemate.object.server.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public abstract class BridgeObject<DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends BridgeObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            OBJECT extends BridgeObject<DATA, CHILD_DATA, CHILD, OBJECT, LISTENER>,
            LISTENER extends com.intuso.housemate.api.object.ObjectListener>
        extends HousemateObject<ServerBridgeResources, DATA, CHILD_DATA, CHILD, LISTENER>
        implements RemoteClientListener, ObjectListener<CHILD> {

    private final List<RemoteClient> loadedByClients = Lists.newArrayList();
    private final Map<RemoteClient, ListenerRegistration> clientListeners= Maps.newHashMap();

    protected BridgeObject(ServerBridgeResources resources, DATA data) {
        super(resources, data);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(LOAD_REQUEST, new Receiver<ClientPayload<LoadRequest>>() {
            @Override
            public void messageReceived(Message<ClientPayload<LoadRequest>> message) throws HousemateException {
                RemoteClient client = message.getPayload().getClient();
                String loaderName = message.getPayload().getOriginal().getLoaderName();
                TreeLoadInfo loadInfo = message.getPayload().getOriginal().getLoadInfo();
                String childId = loadInfo.getId();
                if(client.getType() != ConnectionType.Proxy) {
                    getLog().e("Client requesting an object is not of type " + ConnectionType.Proxy);
                    sendMessage(LOAD_RESPONSE, new LoadResponse<HousemateData<?>>(loaderName, new TreeData<HousemateData<?>>(childId, null, null, null), "Connection type is not " + ConnectionType.Proxy.name()), client);
                } else {
                    if(childId.equals(HousemateObject.EVERYTHING_RECURSIVE)
                            || childId.equals(HousemateObject.EVERYTHING)) {
                        for(CHILD child : getChildren()) {
                            TreeData<HousemateData<?>> data = child.getDataForClient(client, loadInfo);
                            sendMessage(LOAD_RESPONSE, new LoadResponse<HousemateData<?>>(loaderName, data), client);
                        }
                        sendMessage(LOAD_RESPONSE, new LoadResponse<HousemateData<?>>(loaderName, new TreeData<HousemateData<?>>(childId, null, null, null)), client);
                    } else {
                        BridgeObject<?, ?, ?, ?, ?> child = getChild(childId);
                        if (child == null) {
                            getLog().w("Load request received from " + Arrays.toString(message.getRoute().toArray(new String[message.getRoute().size()])) + " for non-existant object \"" + childId + "\"");
                            sendMessage(LOAD_RESPONSE, new LoadResponse<HousemateData<?>>(loaderName, new TreeData<HousemateData<?>>(childId, null, null, null), "Object does not exist or you do not have permission to see it"), client);
                        } else {
                            TreeData<HousemateData<?>> data = child.getDataForClient(client, loadInfo);
                            sendMessage(LOAD_RESPONSE, new LoadResponse<HousemateData<?>>(loaderName, data), client);
                        }
                    }
                }
            }
        }));
        result.add(addChildListener(this));
        return result;
    }

    @Override
    public void disconnected(RemoteClient client) {
        if(clientListeners.containsKey(client))
            clientListeners.remove(client).removeListener();
        loadedByClients.remove(client);
    }

    @Override
    public void connectionLost(RemoteClient client) {
        // do nothing
    }

    @Override
    public void reconnected(RemoteClient client) {
        // do nothing
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

    protected TreeData<HousemateData<?>> getDataForClient(RemoteClient client, TreeLoadInfo loadInfo) {
        clientListeners.put(client, client.addListener(this));
        loadedByClients.add(client);
        Map<String, TreeData<?>> children = Maps.newHashMap();
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
                else
                    children.put(entry.getKey(), getChild(entry.getKey()).getDataForClient(client, entry.getValue()));
            }
            for(CHILD child : getChildren())
                childData.put(child.getId(), new ChildOverview(child.getId(), child.getName(), child.getDescription()));
        }
        return new TreeData<HousemateData<?>>(getId(), getData().clone(), children, childData);
    }

    protected void sendMessage(String type, Message.Payload payload, RemoteClient client) throws HousemateException {
        client.sendMessage(getPath(), type, payload);
    }

    protected <MV extends Message.Payload> void broadcastMessage(String type, MV content) {
        for(RemoteClient client : loadedByClients) {
            try {
                if(client.isCurrentlyConnected())
                    sendMessage(type, content, client);
                else
                    getLog().w("Not sending message to client " + client.getConnectionId() + " as it's not currently connected");
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
