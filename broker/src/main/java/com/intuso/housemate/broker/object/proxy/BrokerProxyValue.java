package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.value.ValueWrappable;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 10/05/13
* Time: 00:46
* To change this template use File | Settings | File Templates.
*/
public class BrokerProxyValue extends BrokerProxyValueBase<ValueWrappable, NoChildrenWrappable,
            NoChildrenBrokerProxyObject, BrokerProxyValue> {
    public BrokerProxyValue(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ValueWrappable value) {
        super(resources, value);
    }
}
