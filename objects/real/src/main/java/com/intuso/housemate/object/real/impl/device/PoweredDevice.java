package com.intuso.housemate.object.real.impl.device;

import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.device.feature.RealPowerControl;
import com.intuso.utilities.log.Log;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class PoweredDevice
        extends RealDevice
        implements RealPowerControl {

    /**
     * @param log {@inheritDoc}
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     */
	public PoweredDevice(Log log, String id, String name, String description) {
		super(log, id, name, description);
	}
}
