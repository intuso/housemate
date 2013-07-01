package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.NoChildrenWrappable;

public class NoChildrenProxyObjectFactory
        implements HousemateObjectFactory<ProxyResources<NoChildrenProxyObjectFactory>, NoChildrenWrappable, NoChildrenProxyObject> {
    @Override
    public NoChildrenProxyObject create(ProxyResources<NoChildrenProxyObjectFactory> resources, NoChildrenWrappable data) throws HousemateException {
        throw new HousemateException("Cannot create a child for an object that has no children");
    }
}
