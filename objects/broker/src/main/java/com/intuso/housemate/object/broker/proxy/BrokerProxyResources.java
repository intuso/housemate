package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.object.broker.BrokerResources;

/**
 * @param <FACTORY> the type of the factory
 */
public interface BrokerProxyResources<
        FACTORY extends HousemateObjectFactory<?, ?, ?>> extends BrokerResources<BrokerProxyRootObject> {
    public FACTORY getFactory();
    <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>>
        BrokerProxyResources<NF> cloneForNewFactory(NF newFactory);
}
