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
@TypeInfo(id = "rfxcom-temperature-3", name = "Temperature3", description = "Temperature3 Sensor")
public class Temperature3Sensor extends TemperatureSensor {

	@Inject
	public Temperature3Sensor(@Assisted Logger logger, @Assisted FeatureDriver.Callback driverCallback) {
		super(Temperature3Handler.INSTANCE, driverCallback);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature3Handler.INSTANCE.makeSensor(sensorId);
	}
}
