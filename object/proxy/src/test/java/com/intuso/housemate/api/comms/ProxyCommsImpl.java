package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 05/03/12
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class ProxyCommsImpl extends Router {

    private TestRealRoot realRoot;

    public ProxyCommsImpl(Resources resources) {
        super(resources);
        connect();
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
            getLog().e("Could not send message to real root");
            getLog().st(e);
        }
    }
}
