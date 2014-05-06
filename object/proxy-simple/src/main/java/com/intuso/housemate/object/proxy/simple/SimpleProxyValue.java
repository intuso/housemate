package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.NoChildrenProxyObject;
import com.intuso.housemate.object.proxy.ProxyValue;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:21
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyValue extends ProxyValue<SimpleProxyType, SimpleProxyValue> {

    private final Injector injector;

    @Inject
    public SimpleProxyValue(Log log,
                            ListenersFactory listenersFactory,
                            Injector injector,
                            @Assisted ValueData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
