package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.utilities.log.Log;

import javax.annotation.Nullable;

/**
 */
public class TypeBridge
        extends BridgeObject<TypeWrappable<HousemateObjectWrappable<?>>, HousemateObjectWrappable<?>,
            BridgeObject<?, ?, ?, ?, ?>, TypeBridge, TypeListener>
        implements Type {

    private final static String OPTIONS = "options";
    private final static String SUB_TYPES = "sub-types";

    public TypeBridge(final BrokerBridgeResources resources, Type type) {
        super(resources, cloneWrappable(resources.getLog(), type));
        if(type instanceof HousemateObject && ((HousemateObject)type).getWrapper(OPTIONS) != null) {
            addWrapper(new ListBridge<OptionWrappable, Option<ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge>>, OptionBridge>(resources, (List)((HousemateObject)(type)).getWrapper(OPTIONS),
                    new Function<Option, OptionBridge>() {
                        @Override
                        public OptionBridge apply(@Nullable Option option) {
                            return new OptionBridge(resources, option);
                        }
                    }));
        }
        if(type instanceof HousemateObject && ((HousemateObject)type).getWrapper(SUB_TYPES) != null) {
            addWrapper(new ListBridge<SubTypeWrappable, SubType<?>, SubTypeBridge>(resources, (List)((HousemateObject)(type)).getWrapper(SUB_TYPES),
                    new Function<SubType<?>, SubTypeBridge>() {
                        @Override
                        public SubTypeBridge apply(@Nullable SubType<?> subType) {
                            return new SubTypeBridge(resources, subType);
                        }
                    }));
        }
    }

    private static TypeWrappable<HousemateObjectWrappable<?>> cloneWrappable(Log log, Type type) {
        if(type instanceof HousemateObject)
            return (TypeWrappable<HousemateObjectWrappable<?>>) ((HousemateObject<?, ?, ?, ?, ?>)type).getData().clone();
        else {
            log.e("Cannot bridge to a non-real type. Bridged type will have a null data");
            return null;
        }
    }

    public final static class Converter implements Function<Type, TypeBridge> {

        private BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public TypeBridge apply(@Nullable Type type) {
            return new TypeBridge(resources, type);
        }
    }
}
