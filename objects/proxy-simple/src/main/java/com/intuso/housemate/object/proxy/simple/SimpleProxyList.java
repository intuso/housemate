package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:16
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyList<
            WBL extends HousemateData<?>,
            WR extends ProxyObject<? extends WBL, ?, ?, ?, ?>>
        extends ProxyList<
            WBL,
            WR,
        SimpleProxyList<WBL, WR>> {

    private final Injector injector;

    @Inject
    public SimpleProxyList(Log log,
                           ListenersFactory listenersFactory,
                           Injector injector,
                           @Assisted ListData<WBL> data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected WR createChildInstance(WBL data) {
        return (WR) injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }
}
