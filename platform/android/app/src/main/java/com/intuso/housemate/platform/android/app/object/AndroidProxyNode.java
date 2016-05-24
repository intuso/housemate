package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyNode;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyNode extends ProxyNode<
        AndroidProxyCommand,
        AndroidProxyList<AndroidProxyType>,
        AndroidProxyList<AndroidProxyHardware>,
        AndroidProxyNode> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyNode(Logger logger, ListenersFactory listenersFactory,
                               Factory<AndroidProxyCommand> commandFactory,
                               Factory<AndroidProxyList<AndroidProxyType>> typesFactory,
                               Factory<AndroidProxyList<AndroidProxyHardware>> hardwaresFactory) {
        super(logger, listenersFactory, commandFactory, typesFactory, hardwaresFactory);
    }
}
