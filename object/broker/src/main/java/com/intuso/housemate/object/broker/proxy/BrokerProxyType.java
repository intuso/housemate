package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/01/13
 * Time: 09:01
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyType
        extends BrokerProxyObject<TypeWrappable<HousemateObjectWrappable<?>>,
            HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>, BrokerProxyType, TypeListener>
        implements Type {
    public BrokerProxyType(BrokerProxyResources<BrokerProxyFactory.All> resources, TypeWrappable wrappable) {
        super(resources, wrappable);
    }
}
