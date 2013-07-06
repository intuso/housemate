package com.intuso.housemate.broker.object.bridge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ConnectionType;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.housemate.object.broker.RemoteClientListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.wrapper.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public abstract class BridgeObject<WBL extends HousemateData<SWBL>,
            SWBL extends HousemateData<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>,
            PBO extends BridgeObject<WBL, SWBL, SWR, PBO, L>,
            L extends ObjectListener>
        extends HousemateObject<BrokerBridgeResources, WBL, SWBL, SWR, L>
        implements BaseObject<L>, RemoteClientListener {

    private final List<RemoteClient> loadedByClients = Lists.newArrayList();
    private final Map<RemoteClient, ListenerRegistration> clientListeners= Maps.newHashMap();

    protected BridgeObject(BrokerBridgeResources resources, WBL data) {
        super(resources, data);
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
                    BridgeObject<?, ?, ?, ?, ?> subWrapper = getWrapper(message.getPayload().getOriginal().getChildId());
                    if(subWrapper != null) {
                        long start = System.currentTimeMillis();
                        Data<?> data = subWrapper.getData(message.getPayload().getOriginal(), message.getPayload().getClient());
                        getLog().d("Got data for " + childId + " in " + (System.currentTimeMillis() - start) + "ms");
                        sendMessage(LOAD_RESPONSE, new LoadResponse<Data<?>>(childId, data), message.getPayload().getClient());
                    } else {
                        getLog().w("Load request received from " + Arrays.toString(message.getRoute().toArray(new String[message.getRoute().size()])) + " for non-existant object \"" + message.getPayload().getOriginal().getChildId() + "\"");
                        sendMessage(LOAD_RESPONSE, new LoadResponse<Data<?>>(childId, "Object does not exist or you do not have permission to see it"), message.getPayload().getClient());
                    }
                }
            }
        }));
        return result;
    }

    private HousemateData getData(LoadRequest loadRequest, RemoteClient client) {
        if(!matchesFilter(loadRequest))
            return null;
        HousemateData result = getData().clone();
        for(BridgeObject<?, ?, ?, ?, ?> subWrapper : getWrappers()) {
            HousemateData child = subWrapper.getData(loadRequest, client);
            if(child != null)
                result.addWrappable(child);
        }
        clientListeners.put(client, client.addListener(this));
        loadedByClients.add(client);
        return result;
    }

    protected boolean matchesFilter(LoadRequest loadRequest) {
        return true;
    }

    protected void addLoadedBy(BridgeObject<?, ?, ?, ?, ?> element) {
        element.loadedByClients.addAll(loadedByClients);
        for(BridgeObject<?, ?, ?, ?, ?> child : element.getWrappers())
            addLoadedBy(child);
    }

    protected final PBO getThis() {
        return (PBO)this;
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
}
