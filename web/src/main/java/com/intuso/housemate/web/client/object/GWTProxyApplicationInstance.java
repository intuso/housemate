package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyApplicationInstance;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyApplicationInstance extends ProxyApplicationInstance<
        GWTProxyValue,
        GWTProxyCommand,
        GWTProxyApplicationInstance> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyApplicationInstance(Log log,
                                       ListenersFactory listenersFactory,
                                       GWTGinjector injector,
                                       @Assisted ApplicationInstanceData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
