package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "temperature-5", name = "Temperature5", description = "Temperature5 Sensor")
public class Temperature5Sensor extends TemperatureSensor {

	@Inject
	public Temperature5Sensor(@Assisted DeviceDriver.Callback driverCallback) {
		super(driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return RFXtrx433Hardware.INSTANCE.makeTemperature5(sensorId);
	}
}
