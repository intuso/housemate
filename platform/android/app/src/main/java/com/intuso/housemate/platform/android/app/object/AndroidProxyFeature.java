package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyFeature;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyFeature extends ProxyFeature<AndroidProxyList<AndroidProxyCommand>,
        AndroidProxyList<AndroidProxyValue>,
        AndroidProxyFeature> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyFeature(Logger logger, ListenersFactory listenersFactory, AndroidObjectFactories factories) {
        super(logger, listenersFactory, factories.commands(), factories.values());
    }
}
