package com.intuso.housemate.object.real.impl.device;

import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.device.feature.RealStatefulPowerControl;
import com.intuso.utilities.log.Log;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class StatefulPoweredDevice
        extends RealDevice
        implements RealStatefulPowerControl {

    @com.intuso.housemate.annotations.basic.Values
    public Values values;

    /**
     * @param log {@inheritDoc}
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     */
	public StatefulPoweredDevice(Log log, String id, String name, String description) {
		super(log, id, name, description);
	}

    /**
     * Sets the device to be on
     */
    public final void setOn() {
        values.isOn(true);
    }

    /**
     * Sets the device to be off
     */
    public final void setOff() {
        values.isOn(false);
    }
}
