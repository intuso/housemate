package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.housemate.web.client.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyProperty extends ProxyProperty<
            GWTProxyType,
            GWTProxyCommand,
            GWTProxyProperty> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyProperty(Log log,
                            ListenersFactory listenersFactory,
                            GWTGinjector injector,
                            @Assisted PropertyData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected GWTProxyCommand createChildInstance(CommandData data) {
        return new GWTProxyCommand(getLog(), getListenersFactory(), injector, data);
    }
}
