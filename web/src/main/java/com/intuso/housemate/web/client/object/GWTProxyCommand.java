package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyCommand extends ProxyCommand<
            GWTProxyValue,
            GWTProxyParameter,
            GWTProxyList<ParameterData, GWTProxyParameter>,
            GWTProxyCommand> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyCommand(Log log,
                           ListenersFactory listenersFactory,
                           GWTGinjector injector,
                           @Assisted CommandData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
