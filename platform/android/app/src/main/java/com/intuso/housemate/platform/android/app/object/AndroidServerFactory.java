package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyObject;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 01/09/16.
 */
public class AndroidServerFactory {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Receiver.Factory receiverFactory;
    private final Sender.Factory senderFactory;

    private final ProxyObject.Factory<AndroidProxyServer> server = new Server();

    public AndroidServerFactory(ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, Sender.Factory senderFactory) {
        this.managedCollectionFactory = managedCollectionFactory;
        this.receiverFactory = receiverFactory;
        this.senderFactory = senderFactory;
    }

    public ProxyObject.Factory<AndroidProxyServer> server() {
        return server;
    }

    public class Server implements ProxyObject.Factory<AndroidProxyServer> {

        @Override
        public AndroidProxyServer create(Logger logger) {
            return new AndroidProxyServer(logger, managedCollectionFactory, receiverFactory, senderFactory);
        }
    }
}
