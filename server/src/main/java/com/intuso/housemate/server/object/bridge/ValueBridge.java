package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 10/05/13
* Time: 09:01
* To change this template use File | Settings | File Templates.
*/
public class ValueBridge extends ValueBridgeBase<ValueData, NoChildrenData,
                NoChildrenBridgeObject, Value.Listener<? super ValueBridge>, ValueBridge>
        implements Value<TypeInstances, ValueBridge> {

    public ValueBridge(Logger logger, ListenersFactory listenersFactory, Value<?, ?> proxyValue) {
        super(logger, listenersFactory,
                new ValueData(proxyValue.getId(), proxyValue.getName(), proxyValue.getDescription(), proxyValue.getTypeId(), (TypeInstances) proxyValue.getValue()),
                proxyValue);
    }

    public static class Converter implements Function<Value<?, ?>, ValueBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ValueBridge apply(Value<?, ?> value) {
            return new ValueBridge(logger, listenersFactory, value);
        }
    }
}
