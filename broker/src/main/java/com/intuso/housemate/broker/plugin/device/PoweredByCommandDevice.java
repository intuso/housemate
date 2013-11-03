package com.intuso.housemate.broker.plugin.device;

import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.device.StatefulPoweredDevice;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.io.IOException;

/**
 * Device that allows On/Off functionality by performing a system command.
 */
@FactoryInformation(id = "on-off-command", name = "On Off By Command", description = "Device which runs a configured command to turn things on and off")
public final class PoweredByCommandDevice extends StatefulPoweredDevice {

    private final RealProperty<String> onCommandProperty = StringType.createProperty(getResources(), "on-command", "On Command", "The command to turn the device on", null);

    private final RealProperty<String> offCommandProperty = StringType.createProperty(getResources(), "off-command", "Off Command", "The command to turn the device off", null);

	/**
	 * Construct the device
	 * @param id the name of the device
	 * @throws com.intuso.housemate.api.HousemateException if an error occurs creating the device
	 */
	public PoweredByCommandDevice(RealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
        getCustomPropertyIds().add(onCommandProperty.getId());
        getProperties().add(onCommandProperty);
        getCustomPropertyIds().add(offCommandProperty.getId());
        getProperties().add(offCommandProperty);
	}

    /**
	 * Turn the device on
	 * @throws com.intuso.housemate.api.HousemateException
	 */
    public void turnOn() throws HousemateException {
        String command = onCommandProperty.getTypedValue();
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                setOn();
            } catch(IOException e) {
                throw new HousemateException("Could not run command to turn device on", e);
            }
        } else
            throw new HousemateException("No command set");
    }

	/**
	 * Turn the device off
	 * @throws com.intuso.housemate.api.HousemateException
	 */
    public void turnOff() throws HousemateException {
        String command = offCommandProperty.getTypedValue();
        if(command != null) {
            try {
                Runtime.getRuntime().exec(command);
                setOff();
            } catch(IOException e) {
                throw new HousemateException("Could not run command to turn device off", e);
            }
        } else
            throw new HousemateException("No command set");
    }
}
