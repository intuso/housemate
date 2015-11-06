package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;

/**
 * Created by tomc on 05/11/15.
 */
public class DeviceDriverMapper {

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
