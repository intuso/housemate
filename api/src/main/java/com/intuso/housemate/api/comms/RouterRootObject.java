package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.Reconnect;
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
        implements Root<RouterRootObject, RootListener<? super RouterRootObject>> {

    private final Router router;
    private AuthenticationMethod method = null;

    private String connectionId;

    private Status status = Status.Disconnected;

    protected RouterRootObject(Resources resources, Router router) {
        super(resources, new RootWrappable());
        this.router = router;
        init(null);
    }

    @Override
    public void connect(AuthenticationMethod method) {
        if(this.method != null)
            throw new HousemateRuntimeException("Authentication already in progress/succeeded");
        this.method = method;
        sendMessage(CONNECTION_REQUEST, new AuthenticationRequest(ClientWrappable.Type.ROUTER, method));
    }

    @Override
    public void disconnect() {
        throw new HousemateRuntimeException("It is the router's responsibility to disconnect, not the root object");
    }

    public Router getRouter() {
        return router;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(CONNECTION_RESPONSE, new Receiver<AuthenticationResponse>() {
            @Override
            public void messageReceived(Message<AuthenticationResponse> message) throws HousemateException {
                if(message.getPayload() instanceof ReconnectResponse)
                    status = Status.Connected;
                else
                    connectionId = message.getPayload().getConnectionId();
                method = null;
                status = connectionId != null ? Status.Connected : Status.Disconnected;
                for(RootListener<? super RouterRootObject> listener : getObjectListeners())
                    listener.statusChanged(RouterRootObject.this, status);
            }
        }));
        result.add(addMessageListener(STATUS, new Receiver<Status>() {
            @Override
            public void messageReceived(Message<Status> message) throws HousemateException {
                status = message.getPayload();
                if(status == Status.Connected) {
                    if(connectionId != null) {
                        sendMessage(CONNECTION_REQUEST, new AuthenticationRequest(ClientWrappable.Type.PROXY, new Reconnect(connectionId)));
                        return;
                    } else
                        status = Status.Disconnected;
                }
                for(RootListener<? super RouterRootObject> listener : getObjectListeners())
                    listener.statusChanged(RouterRootObject.this, status);
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
}
