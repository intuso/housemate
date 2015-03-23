package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 10/05/13
* Time: 09:01
* To change this template use File | Settings | File Templates.
*/
public class ValueBridge extends ValueBridgeBase<ValueData, NoChildrenData,
                NoChildrenBridgeObject, ValueBridge> {

    public ValueBridge(Log log, ListenersFactory listenersFactory, Value<?, ?> proxyValue,
                       ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory,
                new ValueData(proxyValue.getId(), proxyValue.getName(), proxyValue.getDescription(), proxyValue.getTypeId(), proxyValue.getTypeInstances()),
                proxyValue, types);
    }

    public static class Converter implements Function<Value<?, ?>, ValueBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;
        }

        @Override
        public ValueBridge apply(Value<?, ?> value) {
            return new ValueBridge(log, listenersFactory, value, types);
        }
    }
}
