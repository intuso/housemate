package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.NoChildrenData;

public class NoChildrenProxyObjectFactory
        implements HousemateObjectFactory<ProxyResources<NoChildrenProxyObjectFactory>, NoChildrenData, NoChildrenProxyObject> {
    @Override
    public NoChildrenProxyObject create(ProxyResources<NoChildrenProxyObjectFactory> resources, NoChildrenData data) throws HousemateException {
        throw new HousemateException("Cannot create a child for an object that has no children");
    }
}
