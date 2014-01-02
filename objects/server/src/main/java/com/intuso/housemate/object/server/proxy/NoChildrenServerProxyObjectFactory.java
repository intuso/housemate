package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.NoChildrenData;

public class NoChildrenServerProxyObjectFactory implements HousemateObjectFactory<ServerProxyResources<?>, NoChildrenData, NoChildrenServerProxyObject> {
    @Override
    public NoChildrenServerProxyObject create(ServerProxyResources<?> resources, NoChildrenData data) throws HousemateException {
        throw new HousemateException("Cannot create a child for an object that has no children");
    }
}
