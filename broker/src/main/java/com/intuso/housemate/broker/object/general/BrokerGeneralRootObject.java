package com.intuso.housemate.broker.object.general;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.broker.comms.RemoteClientManager;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 */
@Singleton
public class BrokerGeneralRootObject
        extends HousemateObject<Resources, RootData, HousemateData<?>,
                    HousemateObject<?, ?, ?, ?, ?>, RootListener<? super BrokerGeneralRootObject>>
        implements Root<BrokerGeneralRootObject> {

    private final RemoteClientManager remoteClientManager;

    @Inject
    public BrokerGeneralRootObject(Resources resources, RemoteClientManager remoteClientManager) {
        super(resources, new RootData());
        this.remoteClientManager = remoteClientManager;
        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return ConnectionStatus.Authenticated;
    }

    @Override
    public String getConnectionId() {
        return null;
    }

    @Override
    public void login(AuthenticationMethod method) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void logout() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Whatcha playing at, fool!?!?");
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CONNECTION_REQUEST_TYPE, new Receiver<ClientPayload<AuthenticationRequest>>() {
            @Override
            public void messageReceived(Message<ClientPayload<AuthenticationRequest>> message) throws HousemateException {
                // process the request
                remoteClientManager.processRequest(message.getPayload().getOriginal(), message.getRoute());
            }
        }));
        result.add(addMessageListener(DISCONNECT_TYPE, new Receiver<ClientPayload<NoPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<NoPayload>> message) throws HousemateException {
                // build the disconnecting client's route as the router's route + the end client id
                remoteClientManager.clientDisconnected(message.getRoute());
            }
        }));
        result.add(addMessageListener(Root.CONNECTION_LOST_TYPE, new Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) throws HousemateException {
                // process the request
                List<String> route = Lists.newArrayList(message.getRoute());
                route.add(message.getPayload().getOriginal().getValue());
                remoteClientManager.connectionLost(route);
            }
        }));
        return result;
    }
}
