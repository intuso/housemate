package com.intuso.housemate.real.impl.device;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.real.RealArgument;
import com.intuso.housemate.real.RealCommand;
import com.intuso.housemate.real.RealDevice;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.RealValue;
import com.intuso.housemate.real.impl.type.BooleanType;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author tclabon
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class OnOffDevice extends RealDevice {

	/**
	 * The On command
	 */
	private final RealCommand onCommand = new RealCommand(getResources(), "on", "Turn On", "Turn the device on", new ArrayList<RealArgument<?>>()) {
		@Override
		public void perform(Map<String, String> values) throws HousemateException {
			getLog().d("Performing \"On\" command");
			turnOn();
		}
	};

	/**
	 * The Off command
	 */
	private final RealCommand offCommand = new RealCommand(getResources(), "off", "Turn Off", "Turn the device off", new ArrayList<RealArgument<?>>()) {
		@Override
		public void perform(Map<String, String> values) throws HousemateException {
			getLog().d("Performing \"Off\" command");
			turnOff();
		}
	};

	/**
	 * The On value
	 */
	private final RealValue<Boolean> onValue = BooleanType.createValue(getResources(), "is-on", "Is On", "True if the device is currently on", false);

	/**
	 * Construct the device
	 * @param id the name of the device
	 * @throws com.intuso.housemate.core.HousemateException if an error occurs creating the device
	 */
	public OnOffDevice(RealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
        getCommands().add(onCommand);
        getCommands().add(offCommand);
        getValues().add(onValue);
	}

	/**
	 * Set the device to be on
	 */
	public final void setOn() {
		onValue.setTypedValue(true);
	}

	/**
	 * Set the device to be off
	 */
	public final void setOff() {
        onValue.setTypedValue(false);
	}

	/**
	 * Turn the device on
	 * @throws com.intuso.housemate.core.HousemateException
	 */
	protected abstract void turnOn() throws HousemateException;

	/**
	 * Turn the device off
	 * @throws com.intuso.housemate.core.HousemateException
	 */
	protected abstract void turnOff() throws HousemateException;
}
