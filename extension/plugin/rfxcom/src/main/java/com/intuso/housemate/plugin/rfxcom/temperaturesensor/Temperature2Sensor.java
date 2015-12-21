package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-temperature-2", name = "Temperature2", description = "Temperature2 Sensor")
public class Temperature2Sensor extends TemperatureSensor {

	@Inject
	public Temperature2Sensor(@Assisted Logger logger, @Assisted DeviceDriver.Callback driverCallback) {
		super(Temperature2Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature2Handler.INSTANCE.makeSensor(sensorId);
	}
}
