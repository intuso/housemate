package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.RemoteLinkedObject;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.Type;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class TypeBridge
        extends BridgeObject<TypeData<HousemateData<?>>, HousemateData<?>,
            BridgeObject<?, ?, ?, ?, ?>, TypeBridge, Type.Listener<? super TypeBridge>>
        implements Type<TypeBridge> {

    private final static String OPTIONS = "options";
    private final static String SUB_TYPES = "sub-types";

    public TypeBridge(Log log, ListenersFactory listenersFactory, Type type) {
        super(log, listenersFactory, cloneData(log, type));
        if(type instanceof RemoteLinkedObject && ((RemoteLinkedObject)type).getChild(OPTIONS) != null) {
            addChild(new ConvertingListBridge<>(log, listenersFactory, (List) ((RemoteLinkedObject) (type)).getChild(OPTIONS),
                    new OptionBridge.Converter(log, listenersFactory)));
        }
        if(type instanceof RemoteLinkedObject && ((RemoteLinkedObject)type).getChild(SUB_TYPES) != null) {
            addChild(new ConvertingListBridge<>(log, listenersFactory, (List) ((RemoteLinkedObject) (type)).getChild(SUB_TYPES),
                    new SubTypeBridge.Converter(log, listenersFactory)));
        }
    }

    private static TypeData<HousemateData<?>> cloneData(Log log, Type type) {
        if(type instanceof RemoteLinkedObject)
            return (TypeData<HousemateData<?>>) ((RemoteLinkedObject<?, ?, ?, ?>)type).getData().clone();
        else {
            log.e("Cannot bridge to a non-real type. Bridged type will have a null data");
            return null;
        }
    }

    public final static class Converter implements Function<Type<?>, TypeBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public TypeBridge apply(Type<?> type) {
            return new TypeBridge(log, listenersFactory, type);
        }
    }
}
