package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyValue;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:26
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyValue extends ProxyValue<AndroidProxyType, AndroidProxyValue> {

    /**
     * @param logger  {@inheritDoc}
     */
    public AndroidProxyValue(Logger logger, ListenersFactory listenersFactory) {
        super(logger, listenersFactory);
    }
}
