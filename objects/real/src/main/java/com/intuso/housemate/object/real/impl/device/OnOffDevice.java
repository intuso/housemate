package com.intuso.housemate.object.real.impl.device;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;

import java.util.ArrayList;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class OnOffDevice extends RealDevice {

	private final RealCommand onCommand = new RealCommand(getResources(), "on", "Turn On", "Turn the device on", new ArrayList<RealParameter<?>>()) {
		@Override
		public void perform(TypeInstanceMap values) throws HousemateException {
			getLog().d("Performing \"On\" command");
			turnOn();
		}
	};

	private final RealCommand offCommand = new RealCommand(getResources(), "off", "Turn Off", "Turn the device off", new ArrayList<RealParameter<?>>()) {
		@Override
		public void perform(TypeInstanceMap values) throws HousemateException {
			getLog().d("Performing \"Off\" command");
			turnOff();
		}
	};

	private final RealValue<Boolean> onValue = BooleanType.createValue(getResources(), "is-on", "Is On", "True if the device is currently on", false);

    /**
     * @param resources {@inheritDoc}
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     */
	public OnOffDevice(RealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
        getCommands().add(onCommand);
        getCommands().add(offCommand);
        getValues().add(onValue);
	}

	/**
	 * Sets the device to be on
	 */
	public final void setOn() {
		onValue.setTypedValues(true);
	}

	/**
	 * Sets the device to be off
	 */
	public final void setOff() {
        onValue.setTypedValues(false);
	}

	/**
	 * Turns the device on
	 * @throws HousemateException if an error occurs
	 */
	protected abstract void turnOn() throws HousemateException;

	/**
	 * Turns the device off
	 * @throws HousemateException if an error occurs
	 */
	protected abstract void turnOff() throws HousemateException;
}
