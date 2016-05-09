package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyRoot;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyRoot extends ProxyRoot<AndroidProxyList<AndroidProxyServer>,
        AndroidProxyRoot> {

    /**
     * @param logger    {@inheritDoc}
     */
    public AndroidProxyRoot(Logger logger, ListenersFactory listenersFactory,
                            Connection connection,
                            ProxyObject.Factory<AndroidProxyList<AndroidProxyServer>> serversFactory) {
        super(logger, listenersFactory, connection, serversFactory);
    }
}
