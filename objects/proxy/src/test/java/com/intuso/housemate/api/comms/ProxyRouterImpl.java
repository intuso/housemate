package com.intuso.housemate.api.comms;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.utilities.log.Log;

/**
 */
public class ProxyRouterImpl extends Router {

    private TestRealRoot realRoot;

    @Inject
    public ProxyRouterImpl(Log log) {
        super(log);
        connect();
        setRouterStatus(Status.ConnectedToServer);
        login(null);
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
