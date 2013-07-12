package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.log.Log;

import javax.annotation.Nullable;

/**
 */
public class TypeBridge
        extends BridgeObject<TypeData<HousemateData<?>>, HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>, TypeBridge, TypeListener>
        implements Type {

    private final static String OPTIONS = "options";
    private final static String SUB_TYPES = "sub-types";

    public TypeBridge(final BrokerBridgeResources resources, Type type) {
        super(resources, cloneData(resources.getLog(), type));
        if(type instanceof HousemateObject && ((HousemateObject)type).getChild(OPTIONS) != null) {
            addChild(new ListBridge<OptionData, Option<ListBridge<SubTypeData, SubType<?>, SubTypeBridge>>, OptionBridge>(resources, (List) ((HousemateObject) (type)).getChild(OPTIONS),
                    new Function<Option, OptionBridge>() {
                        @Override
                        public OptionBridge apply(@Nullable Option option) {
                            return new OptionBridge(resources, option);
                        }
                    }));
        }
        if(type instanceof HousemateObject && ((HousemateObject)type).getChild(SUB_TYPES) != null) {
            addChild(new ListBridge<SubTypeData, SubType<?>, SubTypeBridge>(resources, (List) ((HousemateObject) (type)).getChild(SUB_TYPES),
                    new Function<SubType<?>, SubTypeBridge>() {
                        @Override
                        public SubTypeBridge apply(@Nullable SubType<?> subType) {
                            return new SubTypeBridge(resources, subType);
                        }
                    }));
        }
    }

    private static TypeData<HousemateData<?>> cloneData(Log log, Type type) {
        if(type instanceof HousemateObject)
            return (TypeData<HousemateData<?>>) ((HousemateObject<?, ?, ?, ?, ?>)type).getData().clone();
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
