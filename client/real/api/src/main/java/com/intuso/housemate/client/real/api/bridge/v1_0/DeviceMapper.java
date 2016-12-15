package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;

/**
 * Created by tomc on 03/11/15.
 */
public class DeviceMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>, RealDevice<?, ?, ?, ?, ?, ?>> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>, RealDevice<?, ?, ?, ?, ?, ?>>() {
        @Override
        public RealDevice<?, ?, ?, ?, ?, ?> apply(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?> device) {
            return map(device);
        }
    };

    private final Function<RealDevice<?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>> fromV1_0Function = new Function<RealDevice<?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?> apply(RealDevice<?, ?, ?, ?, ?, ?> device) {
            return map(device);
        }
    };

    private final RealDeviceBridge.Factory bridgeFactory;
    private final RealDeviceBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public DeviceMapper(RealDeviceBridge.Factory bridgeFactory, RealDeviceBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>, RealDevice<?, ?, ?, ?, ?, ?>> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<RealDevice<?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public RealDevice<?, ?, ?, ?, ?, ?> map(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?> device) {
        if(device == null)
            return null;
        if(device instanceof RealDeviceBridge)
            return ((RealDeviceBridge)device).getDevice();
        return reverseBridgeFactory.create(device);
    }

    public com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?> map(RealDevice<?, ?, ?, ?, ?, ?> device) {
        if(device == null)
            return null;
        if(device instanceof RealDeviceBridgeReverse)
            return ((RealDeviceBridgeReverse)device).getDevice();
        return bridgeFactory.create(device);
    }
}
