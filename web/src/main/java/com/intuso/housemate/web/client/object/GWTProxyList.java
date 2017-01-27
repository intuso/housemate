package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyList;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyList<CHILD_DATA extends Object.Data<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>>
        extends ProxyList<CHILD_DATA, CHILD, GWTProxyList<CHILD_DATA, CHILD>> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyList(Logger logger,
                        ListenersFactory managedCollectionFactory,
                        GWTGinjector injector,
                        @Assisted Data<CHILD_DATA> data) {
        super(logger, managedCollectionFactory, data);
        this.injector = injector;
    }

    @Override
    protected CHILD createChild(CHILD_DATA data) {
        return (CHILD) injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
