package com.intuso.housemate.object.real.impl.device;

import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.device.feature.RealPowerControl;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class PoweredDevice
        extends RealDevice
        implements RealPowerControl {

    /**
     * @param resources {@inheritDoc}
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     */
	public PoweredDevice(RealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
	}
}
