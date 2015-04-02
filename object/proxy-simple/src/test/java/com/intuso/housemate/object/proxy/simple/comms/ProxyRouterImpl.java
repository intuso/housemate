package com.intuso.housemate.object.proxy.simple.comms;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.object.proxy.simple.TestRealRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class ProxyRouterImpl extends Router {

    private TestRealRoot realRoot;

    @Inject
    public ProxyRouterImpl(Log log, ListenersFactory listenersFactory, PropertyRepository properties) {
        super(log, listenersFactory, properties);
        connect();
        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
        register(TestEnvironment.APP_DETAILS, "test");
    }

    public void setRealRoot(TestRealRoot realRoot) {
        this.realRoot = realRoot;
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
            if(realRoot != null)
                realRoot.distributeMessage(message);
        } catch(HousemateException e) {
            getLog().e("Could not send message to real root", e);
        }
    }
}
