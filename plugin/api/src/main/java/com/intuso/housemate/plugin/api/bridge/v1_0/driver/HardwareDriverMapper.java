package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.driver.HardwareDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverMapper {

    private final Function<com.intuso.housemate.plugin.api.internal.driver.HardwareDriver, HardwareDriver> toV1_0Function = new Function<com.intuso.housemate.plugin.api.internal.driver.HardwareDriver, HardwareDriver>() {
        @Override
        public HardwareDriver apply(com.intuso.housemate.plugin.api.internal.driver.HardwareDriver hardwareDriver) {
            return map(hardwareDriver);
        }
    };

    private final Function<HardwareDriver, com.intuso.housemate.plugin.api.internal.driver.HardwareDriver> fromV1_0Function = new Function<HardwareDriver, com.intuso.housemate.plugin.api.internal.driver.HardwareDriver>() {
        @Override
        public com.intuso.housemate.plugin.api.internal.driver.HardwareDriver apply(HardwareDriver hardwareDriver) {
            return map(hardwareDriver);
        }
    };

    public Function<com.intuso.housemate.plugin.api.internal.driver.HardwareDriver, HardwareDriver> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<HardwareDriver, com.intuso.housemate.plugin.api.internal.driver.HardwareDriver> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <FROM extends com.intuso.housemate.plugin.api.internal.driver.HardwareDriver, TO extends HardwareDriver>
        TO map(FROM hardwareDriver) {
        if(hardwareDriver == null)
            return null;
        else if(hardwareDriver instanceof HardwareDriverBridge)
            return (TO) ((HardwareDriverBridge) hardwareDriver).getHardwareDriver();
        return (TO) new HardwareDriverBridgeReverse(hardwareDriver);
    }

    public <FROM extends HardwareDriver, TO extends com.intuso.housemate.plugin.api.internal.driver.HardwareDriver>
        TO map(FROM hardwareDriver) {
        if(hardwareDriver == null)
            return null;
        else if(hardwareDriver instanceof HardwareDriverBridgeReverse)
            return (TO) ((HardwareDriverBridgeReverse) hardwareDriver).getHardwareDriver();
        return (TO) new HardwareDriverBridge(hardwareDriver);
    }
}
