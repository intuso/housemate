package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyList<CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>>
        extends ProxyList<CHILD_DATA, CHILD, GWTProxyList<CHILD_DATA, CHILD>> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyList(Log log,
                        ListenersFactory listenersFactory,
                        GWTGinjector injector,
                        @Assisted ListData<CHILD_DATA> data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected CHILD createChildInstance(CHILD_DATA data) {
        return (CHILD) injector.getObjectFactory().create(data);
    }
}
