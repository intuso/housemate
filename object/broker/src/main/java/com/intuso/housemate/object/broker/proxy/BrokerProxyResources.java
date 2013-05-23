package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.object.broker.BrokerResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/03/13
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public interface BrokerProxyResources<F extends HousemateObjectFactory<?, ?, ?>> extends BrokerResources<BrokerProxyRootObject> {
    public F getFactory();
    <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>>
        BrokerProxyResources<NF> cloneForNewFactory(NF newFactory);
}
