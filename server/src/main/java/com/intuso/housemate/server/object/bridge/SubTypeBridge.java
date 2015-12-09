package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.SubTypeData;
import com.intuso.housemate.object.api.internal.SubType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class SubTypeBridge
        extends BridgeObject<SubTypeData, NoChildrenData, NoChildrenBridgeObject, SubTypeBridge, SubType.Listener<? super SubTypeBridge>>
        implements SubType<SubTypeBridge> {

    public SubTypeBridge(Logger logger, ListenersFactory listenersFactory, SubType<?> subType) {
        super(logger, listenersFactory, new SubTypeData(subType.getId(), subType.getName(), subType.getDescription(), subType.getTypeId()));
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    public final static class Converter implements Function<SubType<?>, SubTypeBridge> {

        private final Logger logger;
        private final ListenersFactory listenersFactory;

        public Converter(Logger logger, ListenersFactory listenersFactory) {
            this.logger = logger;
            this.listenersFactory = listenersFactory;
        }

        @Override
        public SubTypeBridge apply(SubType<?> parameter) {
            return new SubTypeBridge(logger, listenersFactory, parameter);
        }
    }
}
