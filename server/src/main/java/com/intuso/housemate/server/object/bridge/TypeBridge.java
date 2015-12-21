package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.RemoteLinkedObject;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.housemate.object.api.internal.Type;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class TypeBridge
        extends BridgeObject<TypeData<HousemateData<?>>, HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>, TypeBridge, Type.Listener<? super TypeBridge>>
        implements Type<TypeBridge> {

    private final static String OPTIONS = "options";
    private final static String SUB_TYPES = "sub-types";

    public TypeBridge(Logger logger, ListenersFactory listenersFactory, Type<?> type) {
        super(listenersFactory, logger, cloneData(logger, type));
        if(type instanceof RemoteLinkedObject && ((RemoteLinkedObject)type).getChild(OPTIONS) != null) {
            addChild(new ConvertingListBridge<>(logger, listenersFactory, (List<Option<?, ?>>) ((RemoteLinkedObject) (type)).getChild(OPTIONS),
                    new OptionBridge.Converter(logger, listenersFactory)));
        }
        if(type instanceof RemoteLinkedObject && ((RemoteLinkedObject)type).getChild(SUB_TYPES) != null) {
            addChild(new ConvertingListBridge<>(logger, listenersFactory, (List<SubType<?>>) ((RemoteLinkedObject) (type)).getChild(SUB_TYPES),
                    new SubTypeBridge.Converter(logger, listenersFactory)));
        }
    }

    private static TypeData<HousemateData<?>> cloneData(Logger logger, Type type) {
        if(type instanceof RemoteLinkedObject)
            return (TypeData<HousemateData<?>>) ((RemoteLinkedObject<?, ?, ?, ?>)type).getData().clone();
        else {
            logger.error("Cannot bridge to a non-real type. Bridged type will have a null data");
            return null;
        }
    }

    public final static class Converter implements Function<Type<?>, TypeBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public TypeBridge apply(Type<?> type) {
            return new TypeBridge(logger, listenersFactory, type);
        }
    }
}
