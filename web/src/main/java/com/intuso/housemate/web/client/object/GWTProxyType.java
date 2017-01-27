package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyType;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyType extends ProxyType<
        Type.Data<Object.Data<?>>,
        Object.Data<?>,
        ProxyObject<?, ?, ?, ?, ?>,
            GWTProxyType> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyType(Logger logger,
                        ListenersFactory managedCollectionFactory,
                        GWTGinjector injector,
                        @Assisted Data<ObjectData<?>> data) {
        super(logger, managedCollectionFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(ObjectData<?> data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
