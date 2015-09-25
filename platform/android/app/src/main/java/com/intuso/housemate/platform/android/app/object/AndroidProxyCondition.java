package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyCondition;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.ConditionData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyCondition extends ProxyCondition<AndroidProxyCommand, AndroidProxyValue,
        AndroidProxyList<PropertyData, AndroidProxyProperty>, AndroidProxyCondition,
        AndroidProxyList<ConditionData, AndroidProxyCondition>> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyCondition(Log log, ListenersFactory listenersFactory, ConditionData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }
}
