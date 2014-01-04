package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.proxy.ServerProxyType;

/**
 */
public class ParameterBridge
        extends BridgeObject<ParameterData, NoChildrenData, NoChildrenBridgeObject, ParameterBridge, ParameterListener>
        implements Parameter<TypeBridge> {

    private final TypeBridge type;

    public ParameterBridge(ServerBridgeResources resources, Parameter<?> parameter,
                           ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(resources, new ParameterData(parameter.getId(), parameter.getName(), parameter.getDescription(), parameter.getTypeId()));
        type = types.get(getData().getType());
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    public final static class Converter implements Function<Parameter<?>, ParameterBridge> {

        private final ServerBridgeResources resources;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(ServerBridgeResources resources, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.resources = resources;
            this.types = types;
        }

        @Override
        public ParameterBridge apply(Parameter<?> parameter) {
            return new ParameterBridge(resources, parameter, types);
        }
    }
}
