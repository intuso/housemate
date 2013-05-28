package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.comms.message.StringMessageValue;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/03/12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class Router implements Sender, Receiver {

    public final static String UNKNOWN_CLIENT = "unknown-client";

    private int nextId = 0;

    private final Map<String, Receiver<?>> keyClientMap;

    private final RouterRootObject root;

    public Router(Resources resources) {
        keyClientMap = new HashMap<String, Receiver<?>>();
        root = new RouterRootObject(resources, this);
    }

    public ListenerRegistration<? super RootListener<? super RouterRootObject>> addRootListener(RootListener<? super RouterRootObject> listener) {
        return root.addObjectListener(listener);
    }

    public void connect(AuthenticationMethod method, AuthenticationResponseHandler responseHandler) {
        root.connect(method, responseHandler);
    }

    public abstract void disconnect();
    
    public synchronized final Registration registerReceiver(Receiver<?> receiver) {
        String clientId = "" + nextId++;
        keyClientMap.put(clientId, receiver);
        return new Registration(clientId, receiver);
    }

    @Override
    public final void messageReceived(Message message) throws HousemateException {
        // get the key
        String key = message.getNextClientKey();

        // if no key then it's a broadcast
        if(key == null) {
            root.distributeMessage(message);
        } else {
            Receiver ms = keyClientMap.get(key);
            if(ms == null)
                root.unknownClient(key);
            else
                ms.messageReceived(message);
        }
    }

    public final class Registration implements Sender {

        private boolean connected= true;
        private final String clientId;
        private final Receiver<?> receiver;

        private Registration(String clientId, Receiver<?> receiver) {
            this.clientId = clientId;
            this.receiver = receiver;
        }

        @Override
        public void sendMessage(Message message) {
            if(!connected)
                throw new HousemateRuntimeException("No longer connected");
            message.addClientKey(clientId);
            Router.this.sendMessage(message);
        }

        public synchronized final void disconnect() {
            connected = false;
            if(clientId != null)
                keyClientMap.remove(clientId);
            Router.this.sendMessage(new Message<StringMessageValue>(new String[] {""}, Root.DISCONNECT,
                    new StringMessageValue(clientId)));
        }
    }
}
