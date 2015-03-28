package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class TypeBridge
        extends BridgeObject<TypeData<HousemateData<?>>, HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>, TypeBridge, TypeListener>
        implements Type {

    private final static String OPTIONS = "options";
    private final static String SUB_TYPES = "sub-types";

    public TypeBridge(Log log, ListenersFactory listenersFactory, Type type) {
        super(log, listenersFactory, cloneData(log, type));
        if(type instanceof HousemateObject && ((HousemateObject)type).getChild(OPTIONS) != null) {
            addChild(new SingleListBridge<OptionData, Option<ListBridge<SubTypeData, SubType<?>, SubTypeBridge>>, OptionBridge>(log, listenersFactory, (List) ((HousemateObject) (type)).getChild(OPTIONS),
                    new OptionBridge.Converter(log, listenersFactory)));
        }
        if(type instanceof HousemateObject && ((HousemateObject)type).getChild(SUB_TYPES) != null) {
            addChild(new SingleListBridge<SubTypeData, SubType<?>, SubTypeBridge>(log, listenersFactory, (List) ((HousemateObject) (type)).getChild(SUB_TYPES),
                    new SubTypeBridge.Converter(log, listenersFactory)));
        }
    }

    private static TypeData<HousemateData<?>> cloneData(Log log, Type type) {
        if(type instanceof HousemateObject)
            return (TypeData<HousemateData<?>>) ((HousemateObject<?, ?, ?, ?>)type).getData().clone();
        else {
            log.e("Cannot bridge to a non-real type. Bridged type will have a null data");
            return null;
        }
    }

    public final static class Converter implements Function<Type, TypeBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public TypeBridge apply(Type type) {
            return new TypeBridge(log, listenersFactory, type);
        }
    }
}
