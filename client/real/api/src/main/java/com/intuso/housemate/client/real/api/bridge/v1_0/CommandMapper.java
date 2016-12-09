package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.object.RealCommand;

/**
 * Created by tomc on 03/11/15.
 */
public class CommandMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?>, RealCommand<?, ?, ?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?>, RealCommand<?, ?, ?>>() {
        @Override
        public RealCommand<?, ?, ?> apply(com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?> command) {
            return map(command);
        }
    };

    private final Function<RealCommand<?, ?, ?>, com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?>> fromV1_0Function = new Function<RealCommand<?, ?, ?>, com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?> apply(RealCommand<?, ?, ?> command) {
            return map(command);
        }
    };

    private final RealCommandBridge.Factory bridgeFactory;
    private final RealCommandBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public CommandMapper(RealCommandBridge.Factory bridgeFactory, RealCommandBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?>, RealCommand<?, ?, ?>> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<RealCommand<?, ?, ?>, com.intuso.housemate.client.real.api.internal.object.RealCommand<?, ?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public RealCommand map(com.intuso.housemate.client.real.api.internal.object.RealCommand command) {
        if(command == null)
            return null;
        if(command instanceof RealCommandBridge)
            return ((RealCommandBridge)command).getCommand();
        return reverseBridgeFactory.create(command);
    }

    public com.intuso.housemate.client.real.api.internal.object.RealCommand map(RealCommand command) {
        if(command == null)
            return null;
        if(command instanceof RealCommandBridgeReverse)
            return ((RealCommandBridgeReverse)command).getCommand();
        return bridgeFactory.create(command);
    }
}
