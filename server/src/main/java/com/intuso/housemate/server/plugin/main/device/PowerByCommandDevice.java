package com.intuso.housemate.server.plugin.main.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.device.feature.RealStatefulPowerControl;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.plugin.api.internal.TypeInfo;

import java.io.IOException;

/**
 * Device that allows On/Off functionality by performing a system command.
 */
@TypeInfo(id = "power-by-command", name = "Power By Command", description = "Device which runs a configured command to turn things on and off")
public final class PowerByCommandDevice implements DeviceDriver, RealStatefulPowerControl {

    @Property(id = "on-command", name = "On Command", description = "The command to turn the device on", typeId = "string")
    private String onCommandProperty;

    @Property(id = "off-command", name = "Off Command", description = "The command to turn the device off", typeId = "string")
    private String offCommandProperty;

    @com.intuso.housemate.client.real.api.internal.annotations.Values
    private Values values;

    @Inject
	public PowerByCommandDevice(@Assisted DeviceDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setOn(boolean on) {
        values.isOn(on);
    }

    /**
	 * Turn the device on
	 */
    public void turnOn() {
        String command = onCommandProperty;
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                setOn(true);
            } catch(IOException e) {
                throw new HousemateCommsException("Could not run command to turn device on", e);
            }
        } else
            throw new HousemateCommsException("No command set");
    }

	/**
	 * Turn the device off
	 */
    public void turnOff() {
        String command = offCommandProperty;
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                setOn(false);
            } catch(IOException e) {
                throw new HousemateCommsException("Could not run command to turn device off", e);
            }
        } else
            throw new HousemateCommsException("No command set");
    }
}
