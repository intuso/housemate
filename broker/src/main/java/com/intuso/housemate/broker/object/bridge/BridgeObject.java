package com.intuso.housemate.broker.object.bridge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.ChildData;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.housemate.object.broker.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.Data;
import com.intuso.utilities.object.ObjectListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public abstract class BridgeObject<DATA extends HousemateData<SWBL>,
            SWBL extends HousemateData<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>,
            PBO extends BridgeObject<DATA, SWBL, SWR, PBO, L>,
            L extends com.intuso.housemate.api.object.ObjectListener>
        extends HousemateObject<BrokerBridgeResources, DATA, SWBL, SWR, L>
        implements RemoteClientListener, ObjectListener<SWR> {

    private final List<RemoteClient> loadedByClients = Lists.newArrayList();
    private final Map<RemoteClient, ListenerRegistration> clientListeners= Maps.newHashMap();

    protected BridgeObject(BrokerBridgeResources resources, DATA data) {
        super(resources, data);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(LOAD_REQUEST, new Receiver<ClientPayload<LoadRequest>>() {
            @Override
            public void messageReceived(Message<ClientPayload<LoadRequest>> message) throws HousemateException {
                String childId = message.getPayload().getOriginal().getChildId();
                if(message.getPayload().getClient().getType() != ConnectionType.Proxy) {
                    getLog().e("Client requesting an object is not of type " + ConnectionType.Proxy);
                    sendMessage(LOAD_RESPONSE, new LoadResponse<Data<?>>(childId, "Connection type is not " + ConnectionType.Proxy.name()), message.getPayload().getClient());
                } else {
                    BridgeObject<?, ?, ?, ?, ?> child = getChild(childId);
                    if(child == null) {
                        getLog().w("Load request received from " + Arrays.toString(message.getRoute().toArray(new String[message.getRoute().size()])) + " for non-existant object \"" + message.getPayload().getOriginal().getChildId() + "\"");
                        sendMessage(LOAD_RESPONSE, new LoadResponse<Data<?>>(childId, "Object does not exist or you do not have permission to see it"), message.getPayload().getClient());
                    } else {
                        HousemateData<?> data = child.getDataForClient(message.getPayload().getClient());
                        HousemateData<?> clonedData = data.clone();
                        List<ChildData> childData = getChildData(data);
                        sendMessage(LOAD_RESPONSE, new LoadResponse<Data<?>>(childId, clonedData, childData), message.getPayload().getClient());
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
    public void childObjectAdded(String childId, SWR child) {
        broadcastMessage(CHILD_ADDED, new ChildData(child.getId(), child.getName(), child.getDescription()));
    }

    @Override
    public void childObjectRemoved(String childId, SWR child) {
        broadcastMessage(CHILD_REMOVED, new ChildData(child.getId(), child.getName(), child.getDescription()));
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        // do nothing
    }

    private HousemateData<?> getDataForClient(RemoteClient client) {
        clientListeners.put(client, client.addListener(this));
        loadedByClients.add(client);
        return getData();
    }

    private List<ChildData> getChildData(HousemateData<?> data) {
        List<ChildData> result = Lists.newArrayList();
        for(HousemateData<?> child : data.getChildData().values())
            result.add(new ChildData(child.getId(), child.getName(), child.getDescription()));
        return result;
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

    protected final PBO getThis() {
        return (PBO)this;
    }
}
