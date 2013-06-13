package com.intuso.housemate.api.comms;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.message.StringMessageValue;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/03/12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class Router implements Sender, Receiver {

    protected enum Status {
        Disconnected,
        Connecting,
        Connected
    }

    private final Log log;

    private final AtomicInteger nextId = new AtomicInteger(-1);
    private final Map<String, Receiver<?>> receivers = Maps.newHashMap();

    private final RouterRootObject root;

    private Status routerStatus = Status.Disconnected;

    public Router(Resources resources) {
        this.log = resources.getLog();
        root = new RouterRootObject(resources, this);
        root.addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
                Message<ConnectionStatus> message = new Message<ConnectionStatus>(new String[] {""}, Root.STATUS, status);
                for(Receiver receiver : receivers.values()) {
                    try {
                        receiver.messageReceived(message);
                    } catch(HousemateException e) {
                        log.e("Failed to notify client of new router status");
                        log.st(e);
                    }
                }
            }

            @Override
            public void brokerInstanceChanged(RouterRootObject root) {
                // do nothing
            }
        });
    }

    public final Log getLog() {
        return log;
    }

    public final ConnectionStatus getStatus() {
        return root.getStatus();
    }

    protected final void setRouterStatus(Status routerStatus) {
        this.routerStatus = routerStatus;
        root.setRouterStatus(routerStatus);
    }

    public ListenerRegistration addObjectListener(RootListener<? super RouterRootObject> listener) {
        return root.addObjectListener(listener);
    }

    public final void login(AuthenticationMethod method) {
        if(routerStatus != Status.Connected)
            throw new HousemateRuntimeException("Cannot login until the router is connected");
        root.login(method);
    }

    public final void logout() {
        root.logout();
    }

    public abstract void connect();
    public abstract void disconnect();
    
    public synchronized final Registration registerReceiver(Receiver<?> receiver) {
        String clientId = "" + nextId.incrementAndGet();
        receivers.put(clientId, receiver);
        return new Registration(clientId);
    }

    @Override
    public final void messageReceived(Message message) throws HousemateException {
        // get the key
        String key = message.getNextClientKey();

        // if no key then it's intended for this router's root object
        if(key == null) {
            root.distributeMessage(message);
        } else {
            Receiver receiver = receivers.get(key);
            if(receiver == null)
                root.unknownClient(key);
            else
                receiver.messageReceived(message);
        }
    }

    public final class Registration implements Sender {

        private boolean connected= true;
        private final String clientId;

        private Registration(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public void sendMessage(Message message) {
            if(!connected)
                throw new HousemateRuntimeException("No longer connected");
            message.addClientKey(clientId);
            Router.this.sendMessage(message);
        }

        public synchronized final void remove() {
            connected = false;
            if(clientId != null)
                receivers.remove(clientId);
        }

        public synchronized final void connectionLost() {
            connected = false;
            if(clientId != null)
                receivers.remove(clientId);
            Router.this.sendMessage(new Message<StringMessageValue>(new String[] {""}, Root.CONNECTION_LOST,
                    new StringMessageValue(clientId)));
        }
    }
}
