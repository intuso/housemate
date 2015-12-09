package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyProperty;
import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyProperty extends ProxyProperty<
            GWTProxyType,
            GWTProxyCommand,
            GWTProxyProperty> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyProperty(Logger logger,
                            ListenersFactory listenersFactory,
                            GWTGinjector injector,
                            @Assisted PropertyData data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected GWTProxyCommand createChildInstance(CommandData data) {
        return new GWTProxyCommand(getLogger(), getListenersFactory(), injector, data);
    }
}
