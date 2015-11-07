package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverMapper {

    private final Function<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, DeviceDriver> toV1_0Function = new Function<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, DeviceDriver>() {
        @Override
        public DeviceDriver apply(com.intuso.housemate.client.real.api.internal.driver.DeviceDriver deviceDriver) {
            return map(deviceDriver);
        }
    };

    private final Function<DeviceDriver, com.intuso.housemate.client.real.api.internal.driver.DeviceDriver> fromV1_0Function = new Function<DeviceDriver, com.intuso.housemate.client.real.api.internal.driver.DeviceDriver>() {
        @Override
        public com.intuso.housemate.client.real.api.internal.driver.DeviceDriver apply(DeviceDriver deviceDriver) {
            return map(deviceDriver);
        }
    };

    public Function<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, DeviceDriver> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<DeviceDriver, com.intuso.housemate.client.real.api.internal.driver.DeviceDriver> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public <FROM extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, TO extends DeviceDriver>
        TO map(FROM deviceDriver) {
        if(deviceDriver == null)
            return null;
        else if(deviceDriver instanceof DeviceDriverBridge)
            return (TO) ((DeviceDriverBridge) deviceDriver).getDeviceDriver();
        return (TO) new DeviceDriverBridgeReverse(deviceDriver);
    }

    public <FROM extends DeviceDriver, TO extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver>
        TO map(FROM deviceDriver) {
        if(deviceDriver == null)
            return null;
        else if(deviceDriver instanceof DeviceDriverBridgeReverse)
            return (TO) ((DeviceDriverBridgeReverse) deviceDriver).getDeviceDriver();
        return (TO) new DeviceDriverBridge(deviceDriver);
    }
}
