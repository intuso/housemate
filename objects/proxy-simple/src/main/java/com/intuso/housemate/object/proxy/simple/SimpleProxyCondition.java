package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyCondition;
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
public final class SimpleProxyCondition extends ProxyCondition<
        SimpleProxyCommand, SimpleProxyValue,
        SimpleProxyList<PropertyData, SimpleProxyProperty>,
        SimpleProxyCondition,
        SimpleProxyList<ConditionData, SimpleProxyCondition>> {

    private final Injector injector;

    @Inject
    public SimpleProxyCondition(Log log,
                                ListenersFactory listenersFactory,
                                Injector injector,
                                @Assisted ConditionData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }
}
