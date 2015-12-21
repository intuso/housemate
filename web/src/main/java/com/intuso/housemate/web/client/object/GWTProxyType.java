package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyType;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyType extends ProxyType<
        TypeData<HousemateData<?>>,
            HousemateData<?>,
        ProxyObject<?, ?, ?, ?, ?>,
            GWTProxyType> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyType(Logger logger,
                        ListenersFactory listenersFactory,
                        GWTGinjector injector,
                        @Assisted TypeData<HousemateData<?>> data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(HousemateData<?> data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
