package com.intuso.housemate.real.impl.device;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.real.RealProperty;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.impl.type.StringType;

import java.io.IOException;

/**
 * @author tclabon
 * Device that allows On/Off functionality by performing a system command.
 */
public final class OnOffCommandDevice extends OnOffDevice {

    private final RealProperty<String> onCommandProperty = StringType.createProperty(getResources(), "on-command", "On Command", "The command to turn the device on", null);

    private final RealProperty<String> offCommandProperty = StringType.createProperty(getResources(), "off-command", "Off Command", "The command to turn the device off", null);

	/**
	 * Construct the device
	 * @param id the name of the device
	 * @throws com.intuso.housemate.core.HousemateException if an error occurs creating the device
	 */
	public OnOffCommandDevice(RealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
        getProperties().add(onCommandProperty);
        getProperties().add(offCommandProperty);
	}

    @Override
    protected void _start() throws HousemateException {
        // do nothing
    }

    @Override
    protected void _stop() {
        // do nothing
    }

    /**
	 * Turn the device on
	 * @throws com.intuso.housemate.core.HousemateException
	 */
	protected void turnOn() throws HousemateException {
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
	 * @throws com.intuso.housemate.core.HousemateException
	 */
	protected void turnOff() throws HousemateException {
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
