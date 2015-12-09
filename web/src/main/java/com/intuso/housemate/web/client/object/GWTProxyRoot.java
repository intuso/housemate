package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ServerData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class GWTProxyRoot extends ProxyRoot<
        GWTProxyServer, GWTProxyList<ServerData, GWTProxyServer>,
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
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
