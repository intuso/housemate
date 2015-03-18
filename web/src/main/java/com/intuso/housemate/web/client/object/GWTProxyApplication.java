package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.object.proxy.ProxyApplication;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyApplication extends ProxyApplication<
        GWTProxyValue,
        GWTProxyCommand,
        GWTProxyApplicationInstance,
        GWTProxyList<ApplicationInstanceData, GWTProxyApplicationInstance>,
        GWTProxyApplication> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyApplication(Log log,
                               ListenersFactory listenersFactory,
                               GWTGinjector injector,
                               @Assisted ApplicationData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
