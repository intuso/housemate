package com.intuso.housemate.object.proxy.simple.comms;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.object.proxy.simple.TestProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class RealRouterImpl extends Router {

    private TestProxyRoot proxyRoot;

    @Inject
    public RealRouterImpl(Log log, ListenersFactory listenersFactory, PropertyRepository properties) {
        super(log, listenersFactory, properties);
        connect();
        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
        register(TestEnvironment.APP_DETAILS, "test");
    }

    public void setProxyRoot(TestProxyRoot proxyRoot) {
        this.proxyRoot = proxyRoot;
    }

    @Override
    public final void connect() {
        // do nothing
    }

    @Override
    public final void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if(proxyRoot != null)
                proxyRoot.distributeMessage(message);
        } catch(HousemateException e) {
            getLog().e("Could not send message to proxy root", e);
        }
    }
}
