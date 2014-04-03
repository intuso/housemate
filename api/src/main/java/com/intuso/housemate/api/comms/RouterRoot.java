package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;

/**
 * Root object used to handle messaging between a router and the server
 */
public class RouterRoot
        extends HousemateObject<RootData, HousemateData<?>,
            HousemateObject<?, ?, ?, ?>, RootListener<? super RouterRoot>>
        implements Root<RouterRoot> {

    private final Router router;
    private final ConnectionManager connectionManager;

    /**
     * @param log the log
     * @param listenersFactory
     * @param router the router to create the root object for
     */
    protected RouterRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router) {
        super(log, listenersFactory, new RootData());
        this.router = router;
        connectionManager = new ConnectionManager(listenersFactory, properties, ClientType.Router);
        connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void statusChanged(ServerConnectionStatus serverConnectionStatus,
                                      ApplicationStatus applicationStatus,
                                      ApplicationInstanceStatus applicationInstanceStatus) {
                for(RootListener<? super RouterRoot> listener : getObjectListeners())
                    listener.statusChanged(RouterRoot.this, serverConnectionStatus, applicationStatus, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for(RootListener<? super RouterRoot> listener : getObjectListeners())
                    listener.newApplicationInstance(RouterRoot.this, instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                for(RootListener<? super RouterRoot> listener : getObjectListeners())
                    listener.newServerInstance(RouterRoot.this, serverId);
            }
        });
        init(null);
    }

    @Override
    public ListenerRegistration addObjectListener(RootListener<? super RouterRoot> listener) {
        return super.addObjectListener(listener);
    }

    public ServerConnectionStatus getServerConnectionStatus() {
        return connectionManager.getServerConnectionStatus();
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return connectionManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return connectionManager.getApplicationInstanceStatus();
    }

    /**
     * Updates the router's connection status
     * @param status the router's new connection status
     */
    protected void setServerConnectionStatus(ServerConnectionStatus status) {
        connectionManager.setConnectionStatus(status, ApplicationStatus.Unregistered, ApplicationInstanceStatus.Unregistered);
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        connectionManager.register(applicationDetails, router);
    }

    @Override
    public void unregister() {
        connectionManager.unregister(router);
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(SERVER_INSTANCE_ID_TYPE, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                connectionManager.setServerInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(APPLICATION_INSTANCE_ID_TYPE, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                connectionManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(CONNECTION_STATUS_TYPE, new Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) throws HousemateException {
                connectionManager.setConnectionStatus(message.getPayload().getServerConnectionStatus(),
                        message.getPayload().getApplicationStatus(), message.getPayload().getApplicationInstanceStatus());
            }
        }));
        return result;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        router.sendMessage(message);
    }

    /**
     * Sends a message
     * @param type the type of the message to send
     * @param payload the message payload
     */
    private void sendMessage(String type, Message.Payload payload) {
        sendMessage(new Message<Message.Payload>(getPath(), type, payload));
    }

    /**
     * Tells the server that a client key it tried to use is unknown
     * @param key the unknown key
     */
    public void unknownClient(String key) {
        sendMessage(Root.CONNECTION_LOST_TYPE, new StringPayload(key));
    }
}
