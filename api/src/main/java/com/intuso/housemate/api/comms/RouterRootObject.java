package com.intuso.housemate.api.comms;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Root object used to handle messaging between a router and the server
 */
public class RouterRootObject
        extends HousemateObject<RootData, HousemateData<?>,
            HousemateObject<?, ?, ?, ?>, RootListener<? super RouterRootObject>>
        implements Root<RouterRootObject>, ConnectionStatusChangeListener {

    private final Router router;
    private final ConnectionManager connectionManager;

    /**
     * @param log the log
     * @param router the router to create the root object for
     */
    protected RouterRootObject(Log log, Router router) {
        super(log, new RootData());
        this.router = router;
        connectionManager = new ConnectionManager(router, ConnectionType.Router, ConnectionStatus.Disconnected);
        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return connectionManager.getStatus();
    }

    /**
     * Updates the router's connection status
     * @param status the router's new connection status
     */
    protected void setRouterStatus(Router.Status status) {
        switch (status) {
            case Disconnected:
            case Connecting:
                connectionManager.routerStatusChanged(ConnectionStatus.Disconnected);
                break;
            case Connected:
                // triggers the router to relog in if it thinks the router that it is connected to is now authenticated.
                connectionManager.routerStatusChanged(ConnectionStatus.Authenticated);
                break;
        }
    }

    @Override
    public String getConnectionId() {
        return connectionManager.getConnectionId();
    }

    @Override
    public void login(AuthenticationMethod method) {
        connectionManager.login(method);
    }

    @Override
    public void logout() {
        connectionManager.logout();
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(connectionManager.addStatusChangeListener(this));
        result.add(addMessageListener(CONNECTION_RESPONSE_TYPE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                connectionManager.authenticationResponseReceived(message.getPayload());
            }
        }));
        result.add(addMessageListener(STATUS_TYPE, new Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) throws HousemateException {
                connectionManager.routerStatusChanged(message.getPayload());
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

    @Override
    public void connectionStatusChanged(ConnectionStatus status) {
        for(RootListener<? super RouterRootObject> listener : Lists.newArrayList(getObjectListeners()))
            listener.connectionStatusChanged(this, status);
    }

    @Override
    public void newServerInstance() {
        for(RootListener<? super RouterRootObject> listener : Lists.newArrayList(getObjectListeners()))
            listener.newServerInstance(this);
    }
}
