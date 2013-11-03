package com.intuso.housemate.object.real.device.feature;

import com.intuso.housemate.annotations.basic.Command;
import com.intuso.housemate.annotations.basic.Value;
import com.intuso.housemate.annotations.feature.Id;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.feature.PowerControl;
import com.intuso.housemate.api.object.device.feature.StatefulPowerControl;

@Id("on-off")
public interface RealStatefulPowerControl extends RealFeature {

    @Command(id = PowerControl.ON_COMMAND, name = "Turn On", description = "Turn the device on")
    void turnOn() throws HousemateException;

    @Command(id = PowerControl.OFF_COMMAND, name = "Turn Off", description = "Turn the device off")
    void turnOff() throws HousemateException;

    public interface Values {
        @Value(id = StatefulPowerControl.IS_ON_VALUE, name = "Is On", description = "True if the device is currently on", typeId = "boolean")
        void isOn(boolean isOn);
    }
}
