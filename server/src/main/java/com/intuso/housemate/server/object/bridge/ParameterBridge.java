package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ParameterData;
import com.intuso.housemate.object.api.internal.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class ParameterBridge
        extends BridgeObject<ParameterData, NoChildrenData, NoChildrenBridgeObject, ParameterBridge, Parameter.Listener<? super ParameterBridge>>
        implements Parameter<ParameterBridge> {

    public ParameterBridge(Log log, ListenersFactory listenersFactory, Parameter<?> parameter) {
        super(log, listenersFactory, new ParameterData(parameter.getId(), parameter.getName(), parameter.getDescription(), parameter.getTypeId()));
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    public final static class Converter implements Function<Parameter<?>, ParameterBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public ParameterBridge apply(Parameter<?> parameter) {
            return new ParameterBridge(log, listenersFactory, parameter);
        }
    }
}
