package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.annotations.TypeInfo;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-temperature-1", name = "Temperature1", description = "Temperature1 Sensor")
public class Temperature1Sensor extends TemperatureSensor {

	@Inject
	public Temperature1Sensor(@Assisted Logger logger, @Assisted DeviceDriver.Callback driverCallback) {
		super(Temperature1Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature1Handler.INSTANCE.makeSensor(sensorId);
	}
}
