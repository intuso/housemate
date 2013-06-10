package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class SubTypeBridge
        extends BridgeObject<SubTypeWrappable, NoChildrenWrappable, NoChildrenBridgeObject, SubTypeBridge, SubTypeListener>
        implements SubType<TypeBridge> {

    private final TypeBridge type;

    public SubTypeBridge(BrokerBridgeResources resources, SubType<?> subType) {
        super(resources, new SubTypeWrappable(subType.getId(), subType.getName(), subType.getDescription(), subType.getType().getId()));
        type = resources.getGeneralResources().getBridgeResources().getRoot().getTypes().get(getWrappable().getType());
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
        public SubTypeBridge apply(@Nullable SubType<?> argument) {
            return new SubTypeBridge(resources, argument);
        }
    }
}
