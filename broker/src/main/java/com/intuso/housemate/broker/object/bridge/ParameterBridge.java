package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;

/**
 */
public class ParameterBridge
        extends BridgeObject<ParameterData, NoChildrenData, NoChildrenBridgeObject, ParameterBridge, ParameterListener>
        implements Parameter<TypeBridge> {

    private final TypeBridge type;

    public ParameterBridge(BrokerBridgeResources resources, Parameter<?> parameter) {
        super(resources, new ParameterData(parameter.getId(), parameter.getName(), parameter.getDescription(), parameter.getType().getId()));
        type = resources.getGeneralResources().getBridgeResources().getRoot().getTypes().get(getData().getType());
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    public final static class Converter implements Function<Parameter<?>, ParameterBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public ParameterBridge apply(Parameter<?> parameter) {
            return new ParameterBridge(resources, parameter);
        }
    }
}
