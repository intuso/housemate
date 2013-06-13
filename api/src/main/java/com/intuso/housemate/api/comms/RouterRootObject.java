package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.message.AuthenticationResponse;
import com.intuso.housemate.api.comms.message.StringMessageValue;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.connection.ClientWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 25/03/13
 * Time: 09:29
 * To change this template use File | Settings | File Templates.
 */
public class RouterRootObject
        extends HousemateObject<Resources, RootWrappable, HousemateObjectWrappable<?>,
            HousemateObject<?, ?, ?, ?, ?>, RootListener<? super RouterRootObject>>
        implements Root<RouterRootObject, RootListener<? super RouterRootObject>>, ConnectionStatusChangeListener {

    private final Router router;
    private final ConnectionManager connectionManager;

    protected RouterRootObject(Resources resources, Router router) {
        super(resources, new RootWrappable());
        this.router = router;
        connectionManager = new ConnectionManager(router, ClientWrappable.Type.Router, ConnectionStatus.Disconnected);
        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return connectionManager.getStatus();
    }

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
        result.add(addMessageListener(CONNECTION_RESPONSE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                connectionManager.authenticationResponseReceived(message.getPayload());
            }
        }));
        result.add(addMessageListener(STATUS, new Receiver<ConnectionStatus>() {
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

    public void sendMessage(String type, Message.Payload payload) {
        sendMessage(new Message<Message.Payload>(getPath(), type, payload));
    }

    public void unknownClient(String key) {
        sendMessage(Root.CONNECTION_LOST, new StringMessageValue(key));
    }

    @Override
    public void connectionStatusChanged(ConnectionStatus status) {
        for(RootListener<? super RouterRootObject> listener : getObjectListeners())
            listener.connectionStatusChanged(this, status);
    }

    @Override
    public void brokerInstanceChanged() {
        for(RootListener<? super RouterRootObject> listener : getObjectListeners())
            listener.brokerInstanceChanged(this);
    }
}
