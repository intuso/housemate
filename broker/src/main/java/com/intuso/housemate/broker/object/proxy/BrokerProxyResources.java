package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerResources;
import com.intuso.housemate.core.object.HousemateObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/03/13
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyResources<F extends HousemateObjectFactory<?, ?, ?>> extends BrokerResources<BrokerProxyRootObject> {

    private final F factory;

    public BrokerProxyResources(BrokerGeneralResources generalResources, F factory) {
        super(generalResources);
        this.factory = factory;
    }

    public F getFactory() {
        return factory;
    }
}
