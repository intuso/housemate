package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverFactoryMapper {

    private Function<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>, DeviceDriver.Factory<?>> toFunction = new Function<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>, DeviceDriver.Factory<?>>() {
        @Override
        public DeviceDriver.Factory<?> apply(com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?> deviceDriverFactory) {
            return map(deviceDriverFactory);
        }
    };

    private final Function<DeviceDriver.Factory<?>, com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>> fromFunction = new Function<DeviceDriver.Factory<?>, com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?> apply(DeviceDriver.Factory<?> deviceDriverFactory) {
            return map(deviceDriverFactory);
        }
    };

    private final DeviceDriverFactoryBridge.Factory bridgeFactory;
    private final DeviceDriverFactoryBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public DeviceDriverFactoryMapper(DeviceDriverFactoryBridge.Factory bridgeFactory, DeviceDriverFactoryBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public Function<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>, DeviceDriver.Factory<?>> getToV1_0Function() {
        return toFunction;
    }

    public Function<DeviceDriver.Factory<?>, com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>> getFromV1_0Function() {
        return fromFunction;
    }

    public DeviceDriver.Factory<?> map(com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?> deviceDriverFactory) {
        if(deviceDriverFactory == null)
            return null;
        else if(deviceDriverFactory instanceof DeviceDriverFactoryBridge)
            return ((DeviceDriverFactoryBridge)deviceDriverFactory).getFactory();
        return reverseBridgeFactory.create(deviceDriverFactory);
    }

    public com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?> map(DeviceDriver.Factory<?> deviceDriverFactory) {
        if(deviceDriverFactory == null)
            return null;
        else if(deviceDriverFactory instanceof DeviceDriverFactoryBridgeReverse)
            return ((DeviceDriverFactoryBridgeReverse)deviceDriverFactory).getFactory();
        return bridgeFactory.create(deviceDriverFactory);
    }
}
