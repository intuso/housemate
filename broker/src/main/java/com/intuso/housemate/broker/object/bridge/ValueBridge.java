package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueData;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 10/05/13
* Time: 09:01
* To change this template use File | Settings | File Templates.
*/
public class ValueBridge extends ValueBridgeBase<ValueData, NoChildrenData,
                NoChildrenBridgeObject, ValueBridge> {
    public ValueBridge(BrokerBridgeResources resources, Value<?, ?> proxyValue) {
        super(resources, new ValueData(proxyValue.getId(), proxyValue.getName(), proxyValue.getDescription(), proxyValue.getType().getId(), proxyValue.getTypeInstances()), proxyValue);
    }

    public static class Converter implements Function<Value<?, ?>, ValueBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ValueBridge apply(Value<?, ?> value) {
            return new ValueBridge(resources, value);
        }
    }
}
