package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.server.ServerData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRoot;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class GWTProxyRoot extends ProxyRoot<
        GWTProxyServer, GWTProxyList<ServerData, GWTProxyServer>,
            GWTProxyRoot> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyRoot(Log log,
                        ListenersFactory listenersFactory,
                        PropertyRepository properties,
                        GWTGinjector injector,
                        Router router) {
        super(log, listenersFactory, properties, router);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
