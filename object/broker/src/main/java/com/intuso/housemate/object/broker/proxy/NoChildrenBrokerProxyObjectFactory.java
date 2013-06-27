package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.NoChildrenWrappable;

public class NoChildrenBrokerProxyObjectFactory implements HousemateObjectFactory<BrokerProxyResources<?>, NoChildrenWrappable, NoChildrenBrokerProxyObject> {
    @Override
    public NoChildrenBrokerProxyObject create(BrokerProxyResources<?> resources, NoChildrenWrappable data) throws HousemateException {
        throw new HousemateException("Cannot create a child for an object that has no children");
    }
}
