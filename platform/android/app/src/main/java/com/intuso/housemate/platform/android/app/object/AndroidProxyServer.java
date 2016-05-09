package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyServer;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyServer extends ProxyServer<AndroidProxyCommand,
        AndroidProxyList<AndroidProxyAutomation>,
        AndroidProxyList<AndroidProxyDevice>,
        AndroidProxyList<AndroidProxyUser>,
        AndroidProxyList<AndroidProxyNode>,
        AndroidProxyServer> {

    /**
     * @param logger    {@inheritDoc}
     */
    public AndroidProxyServer(Logger logger, ListenersFactory listenersFactory,
                              ProxyObject.Factory<AndroidProxyCommand> commandFactory,
                              ProxyObject.Factory<AndroidProxyList<AndroidProxyAutomation>> automationsFactory,
                              ProxyObject.Factory<AndroidProxyList<AndroidProxyDevice>> devicesFactory,
                              ProxyObject.Factory<AndroidProxyList<AndroidProxyUser>> usersFactory,
                              ProxyObject.Factory<AndroidProxyList<AndroidProxyNode>> nodesFactory) {
        super(logger, listenersFactory, commandFactory, automationsFactory, devicesFactory, usersFactory, nodesFactory);
    }
}
