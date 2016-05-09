package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyList;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyList<ELEMENT extends ProxyObject<?, ?>>
        extends ProxyList<ELEMENT, AndroidProxyList<ELEMENT>> {

    /**
     * @param logger  {@inheritDoc}
     */
    public AndroidProxyList(Logger logger, ListenersFactory listenersFactory, ProxyObject.Factory<ELEMENT> factory) {
        super(logger, listenersFactory, factory);
    }
}
