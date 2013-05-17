package com.intuso.housemate.core.comms;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.TestProxyRoot;
import com.intuso.housemate.core.resources.Resources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 05/03/12
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class RealCommsImpl extends Comms {

    private TestProxyRoot proxyRoot;

    public RealCommsImpl(Resources resources) {
        super(resources);
        connect(null, null);
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
            log.e("Could not send message to proxy root");
            log.st(e);
        }
    }
}
