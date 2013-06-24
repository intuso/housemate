package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public class NoChildrenBrokerProxyObject extends BrokerProxyObject<NoChildrenWrappable, NoChildrenWrappable,
            NoChildrenBrokerProxyObject, NoChildrenBrokerProxyObject, ObjectListener> {
    private NoChildrenBrokerProxyObject(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, NoChildrenWrappable wrappable) {
        super(resources, wrappable);
    }
}
