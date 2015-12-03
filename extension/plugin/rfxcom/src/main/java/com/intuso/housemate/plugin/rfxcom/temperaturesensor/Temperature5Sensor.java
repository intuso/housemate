package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-temperature-5", name = "Temperature5", description = "Temperature5 Sensor")
public class Temperature5Sensor extends TemperatureSensor {

	@Inject
	public Temperature5Sensor(@Assisted DeviceDriver.Callback driverCallback) {
		super(Temperature5Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature5Handler.INSTANCE.makeSensor(sensorId);
	}
}
