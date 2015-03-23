package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.ServerProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class SubTypeBridge
        extends BridgeObject<SubTypeData, NoChildrenData, NoChildrenBridgeObject, SubTypeBridge, SubTypeListener>
        implements SubType<TypeBridge> {

    private final TypeBridge type;

    public SubTypeBridge(Log log, ListenersFactory listenersFactory, SubType<?> subType,
                         ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, new SubTypeData(subType.getId(), subType.getName(), subType.getDescription(), subType.getTypeId()));
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

    public final static class Converter implements Function<SubType<?>, SubTypeBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListenersFactory listenersFactory, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.listenersFactory = listenersFactory;
            this.types = types;
        }

        @Override
        public SubTypeBridge apply(SubType<?> parameter) {
            return new SubTypeBridge(log, listenersFactory, parameter, types);
        }
    }
}
