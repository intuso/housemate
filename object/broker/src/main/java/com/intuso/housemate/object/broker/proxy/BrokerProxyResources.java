package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.object.broker.BrokerResources;

/**
 */
public interface BrokerProxyResources<F extends HousemateObjectFactory<?, ?, ?>> extends BrokerResources<BrokerProxyRootObject> {
    public F getFactory();
    <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>>
        BrokerProxyResources<NF> cloneForNewFactory(NF newFactory);
}
