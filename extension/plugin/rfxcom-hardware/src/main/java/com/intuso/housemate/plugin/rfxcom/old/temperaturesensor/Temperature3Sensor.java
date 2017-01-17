package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-temperature-3", name = "Temperature3", description = "Temperature3 Sensor")
public class Temperature3Sensor extends TemperatureSensor {

	@Inject
	public Temperature3Sensor() {
		super(Temperature3Handler.INSTANCE);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature3Handler.INSTANCE.makeSensor(sensorId);
	}
}
