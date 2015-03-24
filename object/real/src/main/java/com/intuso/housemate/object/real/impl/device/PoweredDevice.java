package com.intuso.housemate.object.real.impl.device;

import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.device.feature.RealPowerControl;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Device sub class for all devices that allow simple On/Off functionality. Can be extended again to
 * add extra commands/values and to define any properties the device needs
 */
public abstract class PoweredDevice
        extends RealDevice
        implements RealPowerControl {

	public PoweredDevice(Log log, ListenersFactory listenersFactory, String type, DeviceData data) {
		super(log, listenersFactory, type, data);
	}
}
