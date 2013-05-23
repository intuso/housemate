package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/02/13
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class NoChildrenBrokerProxyObject extends BrokerProxyObject<NoChildrenWrappable, NoChildrenWrappable,
            NoChildrenBrokerProxyObject, NoChildrenBrokerProxyObject, ObjectListener> {
    private NoChildrenBrokerProxyObject(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, NoChildrenWrappable wrappable) {
        super(resources, wrappable);
    }
}
