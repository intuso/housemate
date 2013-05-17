package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.core.object.value.ValueWrappable;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 10/05/13
* Time: 09:01
* To change this template use File | Settings | File Templates.
*/
public class ValueBridge extends ValueBridgeBase<ValueWrappable, NoChildrenWrappable,
                NoChildrenBridgeObject, ValueBridge> {
    public ValueBridge(BrokerBridgeResources resources, Value<?, ?> proxyValue) {
        super(resources, new ValueWrappable(proxyValue.getId(), proxyValue.getName(), proxyValue.getDescription(), proxyValue.getType().getId(), proxyValue.getValue()), proxyValue);
    }
}
