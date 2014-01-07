package com.intuso.housemate.api.comms;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestProxyRoot;
import com.intuso.utilities.log.Log;

/**
 */
public class RealRouterImpl extends Router {

    private TestProxyRoot proxyRoot;

    @Inject
    public RealRouterImpl(Log log) {
        super(log);
        connect();
        setRouterStatus(Status.ConnectedToServer);
        login(null);
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
