package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class SubTypeBridge
        extends BridgeObject<SubTypeData, NoChildrenData, NoChildrenBridgeObject, SubTypeBridge, SubType.Listener<? super SubTypeBridge>>
        implements SubType<SubTypeBridge> {

    public SubTypeBridge(Log log, ListenersFactory listenersFactory, SubType<?> subType) {
        super(log, listenersFactory, new SubTypeData(subType.getId(), subType.getName(), subType.getDescription(), subType.getTypeId()));
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    public final static class Converter implements Function<SubType<?>, SubTypeBridge> {

        private final Log log;
        private final ListenersFactory listenersFactory;

        public Converter(Log log, ListenersFactory listenersFactory) {
            this.log = log;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public SubTypeBridge apply(SubType<?> parameter) {
            return new SubTypeBridge(log, listenersFactory, parameter);
        }
    }
}
