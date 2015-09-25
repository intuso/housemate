package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.access.ServerConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;

/**
 * Root object used to handle messaging between a router and the server
 */
public class RouterRoot
        extends RemoteObject<RootData, HousemateData<?>,
                        RemoteObject<?, ?, ?, ?>, ClientRoot.Listener<? super RouterRoot>>
        implements ClientRoot<ClientRoot.Listener<? super RouterRoot>, RouterRoot>, Message.Sender, Message.Receiver<Message.Payload> {

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
        connectionManager = new ConnectionManager(listenersFactory, properties, ApplicationRegistration.ClientType.Router, this);
        connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus) {
                for(ClientRoot.Listener<? super RouterRoot> listener : getObjectListeners())
                    listener.serverConnectionStatusChanged(RouterRoot.this, serverConnectionStatus);
            }

            @Override
            public void applicationStatusChanged(Application.Status applicationStatus) {
                for(ClientRoot.Listener<? super RouterRoot> listener : getObjectListeners())
                    listener.applicationStatusChanged(RouterRoot.this, applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(ApplicationInstance.Status applicationInstanceStatus) {
                for(ClientRoot.Listener<? super RouterRoot> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(RouterRoot.this, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for(ClientRoot.Listener<? super RouterRoot> listener : getObjectListeners())
                    listener.newApplicationInstance(RouterRoot.this, instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                for(ClientRoot.Listener<? super RouterRoot> listener : getObjectListeners())
                    listener.newServerInstance(RouterRoot.this, serverId);
            }
        });
        init(null);
    }

    public ServerConnectionStatus getServerConnectionStatus() {
        return connectionManager.getServerConnectionStatus();
    }

    @Override
    public Application.Status getApplicationStatus() {
        return connectionManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return connectionManager.getApplicationInstanceStatus();
    }

    /**
     * Updates the router's connection status
     * @param status the router's new connection status
     */
    protected void setServerConnectionStatus(ServerConnectionStatus status) {
        connectionManager.setServerConnectionStatus(status);
    }

    @Override
    public void register(ApplicationDetails applicationDetails, String component) {
        connectionManager.register(applicationDetails, component);
    }

    @Override
    public void unregister() {
        connectionManager.unregister();
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(RootData.SERVER_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                connectionManager.setServerInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                connectionManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.SERVER_CONNECTION_STATUS_TYPE, new Message.Receiver<ServerConnectionStatus>() {
            @Override
            public void messageReceived(Message<ServerConnectionStatus> message) {
                connectionManager.setServerConnectionStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_STATUS_TYPE, new Message.Receiver<ApplicationData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationData.StatusPayload> message) {
                connectionManager.setApplicationStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_STATUS_TYPE, new Message.Receiver<ApplicationInstanceData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceData.StatusPayload> message) {
                connectionManager.setApplicationInstanceStatus(message.getPayload().getStatus());
            }
        }));
        return result;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) {
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
        sendMessage(new Message<>(getPath(), type, payload));
    }

    /**
     * Tells the server that a client key it tried to use is unknown
     * @param key the unknown key
     */
    public void unknownClient(String key) {
        sendMessage(RootData.CONNECTION_LOST_TYPE, new StringPayload(key));
    }
}
