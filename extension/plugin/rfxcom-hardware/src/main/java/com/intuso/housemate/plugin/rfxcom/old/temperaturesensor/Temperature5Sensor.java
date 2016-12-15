package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.Id;
import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-temperature-5", name = "Temperature5", description = "Temperature5 Sensor")
public class Temperature5Sensor extends TemperatureSensor {

	@Inject
	public Temperature5Sensor(@Assisted Logger logger, @Assisted FeatureDriver.Callback driverCallback) {
		super(Temperature5Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature5Handler.INSTANCE.makeSensor(sensorId);
	}
}
