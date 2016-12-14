package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver;
import org.slf4j.Logger;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "rfxcom-temperature-4", name = "Temperature4", description = "Temperature4 Sensor")
public class Temperature4Sensor extends TemperatureSensor {

	@Inject
	public Temperature4Sensor(@Assisted Logger logger, @Assisted FeatureDriver.Callback driverCallback) {
		super(Temperature4Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature4Handler.INSTANCE.makeSensor(sensorId);
	}
}
