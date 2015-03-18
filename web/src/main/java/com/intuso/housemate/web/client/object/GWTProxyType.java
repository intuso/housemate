package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyType extends ProxyType<
            TypeData<HousemateData<?>>,
            HousemateData<?>,
            ProxyObject<?, ?, ?, ?, ?>,
            GWTProxyType> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyType(Log log,
                        ListenersFactory listenersFactory,
                        GWTGinjector injector,
                        @Assisted TypeData<HousemateData<?>> data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
