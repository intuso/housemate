package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyFeature;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyFeature
        extends ProxyFeature<
        GWTProxyList<Command.Data, GWTProxyCommand>,
        GWTProxyList<Value.Data, GWTProxyValue>,
        GWTProxyFeature> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyFeature(Logger logger,
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
