package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ServerData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyRoot extends ProxyRoot<
        AndroidProxyServer, AndroidProxyList<ServerData, AndroidProxyServer>,
        AndroidProxyRoot> {

    private final AndroidProxyFactory factory;

    /**
     * @param logger    {@inheritDoc}
     * @param router The router to connect through
     */
    public AndroidProxyRoot(Logger logger, ListenersFactory listenersFactory, PropertyRepository properties, Router<?> router) {
        super(logger, listenersFactory, properties, router);
        this.factory = new AndroidProxyFactory(listenersFactory);
    }

    @Override
    protected ProxyObject createChild(HousemateData<?> data) {
        return factory.create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
