package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestProxyRoot;
import com.intuso.housemate.api.resources.Resources;

/**
 */
public class RealCommsImpl extends Router {

    private TestProxyRoot proxyRoot;

    public RealCommsImpl(Resources resources) {
        super(resources);
        connect();
        setRouterStatus(Status.Connected);
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
