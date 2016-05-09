package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyRoot;
import com.intuso.housemate.client.v1_0.data.api.Router;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;

/**
 */
public class GWTProxyRoot extends ProxyRoot<
        GWTProxyServer, GWTProxyList<Server.Data, GWTProxyServer>,
            GWTProxyRoot> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyRoot(Logger logger,
                        ListenersFactory listenersFactory,
                        PropertyRepository properties,
                        GWTGinjector injector,
                        Router<?> router) {
        super(logger, listenersFactory, properties, router);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(Data<?> data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
