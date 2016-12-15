package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-temperature-2", name = "Temperature2", description = "Temperature2 Sensor")
public class Temperature2Sensor extends TemperatureSensor {

	@Inject
	public Temperature2Sensor(@Assisted Logger logger, @Assisted FeatureDriver.Callback driverCallback) {
		super(Temperature2Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature2Handler.INSTANCE.makeSensor(sensorId);
	}
}
