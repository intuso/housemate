package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyCondition extends ProxyCondition<
            GWTProxyCommand, GWTProxyValue,
            GWTProxyList<PropertyData, GWTProxyProperty>,
            GWTProxyCondition, GWTProxyList<ConditionData, GWTProxyCondition>> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyCondition(Log log,
                             ListenersFactory listenersFactory,
                             GWTGinjector injector,
                             @Assisted ConditionData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
