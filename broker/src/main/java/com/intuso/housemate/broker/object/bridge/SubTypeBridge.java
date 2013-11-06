package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;

/**
 */
public class SubTypeBridge
        extends BridgeObject<SubTypeData, NoChildrenData, NoChildrenBridgeObject, SubTypeBridge, SubTypeListener>
        implements SubType<TypeBridge> {

    private final TypeBridge type;

    public SubTypeBridge(BrokerBridgeResources resources, SubType<?> subType) {
        super(resources, new SubTypeData(subType.getId(), subType.getName(), subType.getDescription(), subType.getType().getId()));
        type = resources.getGeneralResources().getBridgeResources().getRoot().getTypes().get(getData().getType());
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    public final static class Converter implements Function<SubType<?>, SubTypeBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public SubTypeBridge apply(SubType<?> parameter) {
            return new SubTypeBridge(resources, parameter);
        }
    }
}
