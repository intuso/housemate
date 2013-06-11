package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestProxyRoot;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 05/03/12
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class RealCommsImpl extends Router {

    private TestProxyRoot proxyRoot;

    public RealCommsImpl(Resources resources) {
        super(resources);
        connect(null);
    }

    public void setProxyRoot(TestProxyRoot proxyRoot) {
        this.proxyRoot = proxyRoot;
    }

    @Override
    public void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if(proxyRoot != null)
                proxyRoot.distributeMessage(message);
        } catch(HousemateException e) {
            getLog().e("Could not send message to proxy root");
            getLog().st(e);
        }
    }
}
