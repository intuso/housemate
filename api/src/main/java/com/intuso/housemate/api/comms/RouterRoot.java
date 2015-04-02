package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
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
        connectionManager = new ConnectionManager(listenersFactory, properties, ClientType.Router, this);
        connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus) {
                for(RootListener<? super RouterRoot> listener : getObjectListeners())
                    listener.serverConnectionStatusChanged(RouterRoot.this, serverConnectionStatus);
            }

            @Override
            public void applicationStatusChanged(ApplicationStatus applicationStatus) {
                for(RootListener<? super RouterRoot> listener : getObjectListeners())
                    listener.applicationStatusChanged(RouterRoot.this, applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(ApplicationInstanceStatus applicationInstanceStatus) {
                for(RootListener<? super RouterRoot> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(RouterRoot.this, applicationInstanceStatus);
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
        result.add(addMessageListener(SERVER_CONNECTION_STATUS_TYPE, new Receiver<ServerConnectionStatus>() {
            @Override
            public void messageReceived(Message<ServerConnectionStatus> message) throws HousemateException {
                connectionManager.setServerConnectionStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(APPLICATION_STATUS_TYPE, new Receiver<ApplicationStatus>() {
            @Override
            public void messageReceived(Message<ApplicationStatus> message) throws HousemateException {
                connectionManager.setApplicationStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(APPLICATION_INSTANCE_STATUS_TYPE, new Receiver<ApplicationInstanceStatus>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceStatus> message) throws HousemateException {
                connectionManager.setApplicationInstanceStatus(message.getPayload());
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
        sendMessage(new Message<>(getPath(), type, payload));
    }

    /**
     * Tells the server that a client key it tried to use is unknown
     * @param key the unknown key
     */
    public void unknownClient(String key) {
        sendMessage(Root.CONNECTION_LOST_TYPE, new StringPayload(key));
    }
}
