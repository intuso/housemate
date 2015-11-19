package com.intuso.housemate.server.object.general;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.comms.api.internal.ClientConnection;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.RemoteLinkedObject;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.NoPayload;
import com.intuso.housemate.comms.api.internal.payload.RootData;
import com.intuso.housemate.comms.api.internal.payload.StringPayload;
import com.intuso.housemate.object.api.internal.Root;
import com.intuso.housemate.server.comms.*;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 */
public class ServerGeneralRoot
        extends RemoteLinkedObject<RootData, HousemateData<?>, RemoteLinkedObject<?, ?, ?, ?>, Root.Listener<? super ServerGeneralRoot>>
        implements Root<Root.Listener<? super ServerGeneralRoot>, ServerGeneralRoot>, Message.Receiver<Message.Payload> {

    public final static Set<String> TYPES = Collections.unmodifiableSet(Sets.newHashSet(
            ApplicationRegistration.APPLICATION_REGISTRATION_TYPE,
            ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE,
            Router.ROUTER_CONNECTED));

    private final Injector injector;
    private final AccessManager accessManager;

    @Inject
    public ServerGeneralRoot(Log log, ListenersFactory listenersFactory, Injector injector, AccessManager accessManager) {
        super(log, listenersFactory, new RootData());
        this.injector = injector;
        this.accessManager = accessManager;
        init(null);
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) {
        distributeMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ApplicationRegistration.APPLICATION_REGISTRATION_TYPE, new Message.Receiver<ClientPayload<ApplicationRegistration>>() {
            @Override
            public void messageReceived(Message<ClientPayload<ApplicationRegistration>> message) {
                getLog().d("Access request received");
                // get the client for the request
                ClientInstance clientInstance = accessManager.getClientApplicationInstance(message.getRoute(), message.getPayload().getOriginal());
                RemoteClient client = injector.getInstance(RemoteClientManager.class).getClient(clientInstance, message.getRoute());
                client.clearState();
                accessManager.initialiseClient(client);
            }
        }));
        result.add(addMessageListener(ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE, new Message.Receiver<ClientPayload<NoPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<NoPayload>> message) {
                // build the disconnecting client's route as the router's route + the end client id
                injector.getInstance(RemoteClientManager.class).clientUnregistered(message.getRoute());
            }
        }));
        result.add(addMessageListener(Router.ROUTER_CONNECTED, new Message.Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) {
                String routerId = message.getPayload().getOriginal().getValue();
                getLog().d("Router connected, id = " + routerId);
                // process the request
                ClientInstance clientInstance = accessManager.getClientRouterInstance(message.getRoute(), message.getPayload().getOriginal().getValue());
                RemoteClient client = injector.getInstance(RemoteClientManager.class).getClient(clientInstance, message.getRoute());
                accessManager.initialiseClient(client);
            }
        }));
        result.add(addMessageListener(ClientConnection.UNKNOWN_CLIENT_ID, new Message.Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) {
                // process the request
                List<String> route = Lists.newArrayList(message.getRoute());
                route.add(message.getPayload().getOriginal().getValue());
                injector.getInstance(RemoteClientManager.class).connectionLost(route);
            }
        }));
        return result;
    }
}
