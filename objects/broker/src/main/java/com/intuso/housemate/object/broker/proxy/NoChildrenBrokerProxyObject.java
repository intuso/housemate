package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

public class NoChildrenBrokerProxyObject extends BrokerProxyObject<NoChildrenData, NoChildrenData,
            NoChildrenBrokerProxyObject, NoChildrenBrokerProxyObject, ObjectListener> {
    private NoChildrenBrokerProxyObject(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, NoChildrenData data) {
        super(resources, data);
    }
}
