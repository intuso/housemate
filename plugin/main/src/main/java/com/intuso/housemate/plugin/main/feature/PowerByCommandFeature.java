package com.intuso.housemate.plugin.main.feature;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.real.api.internal.feature.StatefulPowerControl;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Feature that allows On/Off functionality by performing a system command.
 */
@TypeInfo(id = "power-by-command", name = "Power By Command", description = "Feature which runs a configured command to turn things on and off")
public final class PowerByCommandFeature implements FeatureDriver, StatefulPowerControl {

    @Property("string")
    @TypeInfo(id = "on-command", name = "On Command", description = "The command to turn the feature on")
    private String onCommandProperty;

    @Property("string")
    @TypeInfo(id = "off-command", name = "Off Command", description = "The command to turn the feature off")
    private String offCommandProperty;

    private StatefulPowerControl.PowerValues powerValues;

    @Inject
	public PowerByCommandFeature(@Assisted Logger logger, @Assisted FeatureDriver.Callback callback) {}

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    /**
	 * Turn the feature on
	 */
    @Override
    public void turnOn() {
        String command = onCommandProperty;
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                powerValues.isOn(true);
            } catch(IOException e) {
                throw new HousemateException("Could not run command to turn feature on", e);
            }
        } else
            throw new HousemateException("No command set");
    }

	/**
	 * Turn the feature off
	 */
	@Override
    public void turnOff() {
        String command = offCommandProperty;
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                powerValues.isOn(false);
            } catch(IOException e) {
                throw new HousemateException("Could not run command to turn feature off", e);
            }
        } else
            throw new HousemateException("No command set");
    }
}
