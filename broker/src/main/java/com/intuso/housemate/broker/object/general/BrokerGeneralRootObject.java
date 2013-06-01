package com.intuso.housemate.broker.object.general;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.StringMessageValue;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 19/03/13
 * Time: 08:56
 * To change this template use File | Settings | File Templates.
 */
public class BrokerGeneralRootObject
        extends HousemateObject<BrokerGeneralResources, RootWrappable, HousemateObjectWrappable<?>,
                    HousemateObject<?, ?, ?, ?, ?>, RootListener<? super BrokerGeneralRootObject>>
        implements Root<BrokerGeneralRootObject, RootListener<? super BrokerGeneralRootObject>> {

    public BrokerGeneralRootObject(BrokerGeneralResources resources) {
        super(resources, new RootWrappable());

        init(null);
    }

    @Override
    public void connect(AuthenticationMethod method, AuthenticationResponseHandler responseHandler) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void disconnect() {
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
        result.add(addMessageListener(CONNECTION_REQUEST, new Receiver<ClientPayload<AuthenticationRequest>>() {
            @Override
            public void messageReceived(Message<ClientPayload<AuthenticationRequest>> message) throws HousemateException {
                // process the request
                getResources().getRemoteClientManager().processRequest(message.getPayload().getOriginal(), message.getRoute());
            }
        }));
        result.add(addMessageListener(DISCONNECT, new Receiver<ClientPayload<StringMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringMessageValue>> message) throws HousemateException {
                // build the disconnecting client's route as the router's route + the end client id
                List<String> route = Lists.newArrayList(message.getRoute());
                route.add(message.getPayload().getOriginal().getValue());
                getResources().getRemoteClientManager().clientDisconnected(route);
            }
        }));
        result.add(addMessageListener(Router.CONNECTION_LOST, new Receiver<ClientPayload<StringMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringMessageValue>> message) throws HousemateException {
                // process the request
                List<String> route = Lists.newArrayList(message.getRoute());
                route.add(message.getPayload().getOriginal().getValue());
                getResources().getRemoteClientManager().connectionLost(route);
            }
        }));
        return result;
    }
}
