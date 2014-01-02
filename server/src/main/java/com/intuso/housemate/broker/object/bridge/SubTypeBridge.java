package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.broker.proxy.BrokerProxyType;

/**
 */
public class SubTypeBridge
        extends BridgeObject<SubTypeData, NoChildrenData, NoChildrenBridgeObject, SubTypeBridge, SubTypeListener>
        implements SubType<TypeBridge> {

    private final TypeBridge type;

    public SubTypeBridge(BrokerBridgeResources resources, SubType<?> subType,
                         ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
        super(resources, new SubTypeData(subType.getId(), subType.getName(), subType.getDescription(), subType.getType().getId()));
        type = types.get(getData().getType());
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    public final static class Converter implements Function<SubType<?>, SubTypeBridge> {

        private final BrokerBridgeResources resources;
        private final ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types;

        public Converter(BrokerBridgeResources resources, ListBridge<TypeData<?>, BrokerProxyType, TypeBridge> types) {
            this.resources = resources;
            this.types = types;
        }

        @Override
        public SubTypeBridge apply(SubType<?> parameter) {
            return new SubTypeBridge(resources, parameter, types);
        }
    }
}
