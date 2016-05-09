package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxySubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxySubType extends ProxySubType<AndroidProxyType, AndroidProxySubType> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxySubType(Logger logger, ListenersFactory listenersFactory, ProxyObject.Factory<AndroidProxyType> typeFactory) {
        super(logger, listenersFactory, typeFactory);
    }
}
