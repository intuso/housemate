package com.intuso.housemate.server.object.general;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.server.ClientInstance;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.housemate.object.server.RemoteClient;
import com.intuso.housemate.server.comms.AccessManager;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public class ServerGeneralRoot
        extends HousemateObject<RootData, HousemateData<?>,
                    HousemateObject<?, ?, ?, ?>, RootListener<? super ServerGeneralRoot>>
        implements Root<ServerGeneralRoot> {

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
    public ApplicationStatus getApplicationStatus() {
        return ApplicationStatus.AllowInstances;
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return ApplicationInstanceStatus.Allowed;
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void unregister() {
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
        result.add(addMessageListener(APPLICATION_REGISTRATION_TYPE, new Receiver<ClientPayload<ApplicationRegistration>>() {
            @Override
            public void messageReceived(Message<ClientPayload<ApplicationRegistration>> message) throws HousemateException {
                getLog().d("Access request received");
                // get the client for the request
                ClientInstance clientInstance = accessManager.getClientInstance(message.getPayload().getOriginal());
                RemoteClient client = injector.getInstance(RemoteClientManager.class).getClient(clientInstance, message.getRoute());
                accessManager.sendAccessStatusToClient(client);
            }
        }));
        result.add(addMessageListener(APPLICATION_UNREGISTRATION_TYPE, new Receiver<ClientPayload<NoPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<NoPayload>> message) throws HousemateException {
                // build the disconnecting client's route as the router's route + the end client id
                injector.getInstance(RemoteClientManager.class).clientDisconnected(message.getRoute());
            }
        }));
        result.add(addMessageListener(Root.CONNECTION_LOST_TYPE, new Receiver<ClientPayload<StringPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringPayload>> message) throws HousemateException {
                // process the request
                List<String> route = Lists.newArrayList(message.getRoute());
                route.add(message.getPayload().getOriginal().getValue());
                injector.getInstance(RemoteClientManager.class).connectionLost(route);
            }
        }));
        return result;
    }
}
