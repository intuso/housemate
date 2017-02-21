package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.object.ProxyNode;
import com.intuso.utilities.collection.ManagedCollectionFactory;
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
    protected AndroidProxyNode(Logger logger, ManagedCollectionFactory managedCollectionFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, factories.command(), factories.types(), factories.hardwares());
    }
}
