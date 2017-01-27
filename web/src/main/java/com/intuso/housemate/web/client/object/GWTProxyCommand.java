package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Parameter;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyCommand;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyCommand extends ProxyCommand<
            GWTProxyValue,
            GWTProxyParameter,
            GWTProxyList<Parameter.Data, GWTProxyParameter>,
            GWTProxyCommand> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyCommand(Logger logger,
                           ListenersFactory managedCollectionFactory,
                           GWTGinjector injector,
                           @Assisted Data data) {
        super(logger, managedCollectionFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(Object.Data<?> data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
