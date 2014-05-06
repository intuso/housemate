package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyTask extends ProxyTask<
        SimpleProxyCommand,
        SimpleProxyValue,
        SimpleProxyList<PropertyData, SimpleProxyProperty>,
        SimpleProxyTask> {

    private final Injector injector;

    @Inject
    public SimpleProxyTask(Log log,
                           ListenersFactory listenersFactory,
                           Injector injector,
                           @Assisted TaskData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }
}
