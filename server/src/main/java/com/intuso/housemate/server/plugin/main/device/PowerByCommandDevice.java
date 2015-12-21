package com.intuso.housemate.server.plugin.main.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.device.feature.StatefulPowerControl;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Device that allows On/Off functionality by performing a system command.
 */
@TypeInfo(id = "power-by-command", name = "Power By Command", description = "Device which runs a configured command to turn things on and off")
public final class PowerByCommandDevice implements DeviceDriver, StatefulPowerControl {

    @Property("string")
    @TypeInfo(id = "on-command", name = "On Command", description = "The command to turn the device on")
    private String onCommandProperty;

    @Property("string")
    @TypeInfo(id = "off-command", name = "Off Command", description = "The command to turn the device off")
    private String offCommandProperty;

    private PowerValues powerValues;

    @Inject
	public PowerByCommandDevice(@Assisted Logger logger, @Assisted DeviceDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    /**
	 * Turn the device on
	 */
    public void turnOn() {
        String command = onCommandProperty;
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                powerValues.isOn(true);
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
                powerValues.isOn(false);
            } catch(IOException e) {
                throw new HousemateCommsException("Could not run command to turn device off", e);
            }
        } else
            throw new HousemateCommsException("No command set");
    }
}
