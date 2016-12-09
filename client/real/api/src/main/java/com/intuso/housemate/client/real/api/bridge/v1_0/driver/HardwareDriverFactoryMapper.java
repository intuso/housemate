package com.intuso.housemate.client.real.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverFactoryMapper {

    private final HardwareDriverFactoryBridge.Factory bridgeFactory;
    private final HardwareDriverFactoryBridgeReverse.Factory reverseBridgeFactory;

    @Inject
    public HardwareDriverFactoryMapper(HardwareDriverFactoryBridge.Factory bridgeFactory, HardwareDriverFactoryBridgeReverse.Factory reverseBridgeFactory) {
        this.bridgeFactory = bridgeFactory;
        this.reverseBridgeFactory = reverseBridgeFactory;
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.HardwareDriver, TO extends HardwareDriver>
    Function<com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<FROM>, HardwareDriver.Factory<TO>> getToV1_0Function() {
        return new Function<com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<FROM>, HardwareDriver.Factory<TO>>() {
            @Override
            public HardwareDriver.Factory<TO> apply(com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<FROM> hardwareDriverFactory) {
                return map(hardwareDriverFactory);
            }
        };
    }

    public <FROM extends HardwareDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.HardwareDriver>
        Function<HardwareDriver.Factory<FROM>, com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<TO>> getFromV1_0Function() {
        return new Function<HardwareDriver.Factory<FROM>, com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<TO>>() {
            @Override
            public com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<TO> apply(HardwareDriver.Factory<FROM> hardwareDriverFactory) {
                return map(hardwareDriverFactory);
            }
        };
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.HardwareDriver, TO extends HardwareDriver>
        HardwareDriver.Factory<TO> map(com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<FROM> hardwareDriverFactory) {
        if(hardwareDriverFactory == null)
            return null;
        else if(hardwareDriverFactory instanceof HardwareDriverFactoryBridge)
            return (HardwareDriver.Factory<TO>) ((HardwareDriverFactoryBridge)hardwareDriverFactory).getFactory();
        return (HardwareDriver.Factory<TO>) reverseBridgeFactory.create(hardwareDriverFactory);
    }

    public <FROM extends HardwareDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.HardwareDriver>
        com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<TO> map(HardwareDriver.Factory<FROM> hardwareDriverFactory) {
        if(hardwareDriverFactory == null)
            return null;
        else if(hardwareDriverFactory instanceof HardwareDriverFactoryBridgeReverse)
            return (com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<TO>) ((HardwareDriverFactoryBridgeReverse)hardwareDriverFactory).getFactory();
        return (com.intuso.housemate.client.real.api.internal.driver.HardwareDriver.Factory<TO>) bridgeFactory.create(hardwareDriverFactory);
    }
}
