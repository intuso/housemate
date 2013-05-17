package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.NoChildrenWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 20/02/13
 * Time: 19:38
 * To change this template use File | Settings | File Templates.
 */
public class NoChildrenBrokerProxyObjectFactory implements HousemateObjectFactory<BrokerProxyResources<?>, NoChildrenWrappable, NoChildrenBrokerProxyObject> {
    @Override
    public NoChildrenBrokerProxyObject create(BrokerProxyResources<?> resources, NoChildrenWrappable wrappable) throws HousemateException {
        throw new HousemateException("Cannot create a child for an object that has no children");
    }
}
