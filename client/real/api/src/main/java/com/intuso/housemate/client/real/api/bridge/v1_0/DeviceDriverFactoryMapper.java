package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverFactoryMapper {

    private final DeviceDriverFactoryBridge.Factory bridgeFactory;
    private final DeviceDriverFactoryBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public DeviceDriverFactoryMapper(DeviceDriverFactoryBridge.Factory bridgeFactory, DeviceDriverFactoryBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, TO extends DeviceDriver>
    Function<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM>, DeviceDriver.Factory<TO>> getToV1_0Function() {
        return new Function<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM>, DeviceDriver.Factory<TO>>() {
            @Override
            public DeviceDriver.Factory<TO> apply(com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM> deviceDriverFactory) {
                return map(deviceDriverFactory);
            }
        };
    }

    public <FROM extends DeviceDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver>
        Function<DeviceDriver.Factory<FROM>, com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<TO>> getFromV1_0Function() {
        return new Function<DeviceDriver.Factory<FROM>, com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<TO>>() {
            @Override
            public com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<TO> apply(DeviceDriver.Factory<FROM> deviceDriverFactory) {
                return map(deviceDriverFactory);
            }
        };
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, TO extends DeviceDriver>
        DeviceDriver.Factory<TO> map(com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM> deviceDriverFactory) {
        if(deviceDriverFactory == null)
            return null;
        else if(deviceDriverFactory instanceof DeviceDriverFactoryBridge)
            return (DeviceDriver.Factory<TO>) ((DeviceDriverFactoryBridge)deviceDriverFactory).getFactory();
        return (DeviceDriver.Factory<TO>) reverseBridgeFactory.create(deviceDriverFactory);
    }

    public <FROM extends DeviceDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver>
        com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<TO> map(DeviceDriver.Factory<FROM> deviceDriverFactory) {
        if(deviceDriverFactory == null)
            return null;
        else if(deviceDriverFactory instanceof DeviceDriverFactoryBridgeReverse)
            return (com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<TO>) ((DeviceDriverFactoryBridgeReverse)deviceDriverFactory).getFactory();
        return (com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<TO>) bridgeFactory.create(deviceDriverFactory);
    }
}
