package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-temperature-4", name = "Temperature4", description = "Temperature4 Sensor")
public class Temperature4Sensor extends TemperatureSensor {

	@Inject
	public Temperature4Sensor() {
		super(Temperature4Handler.INSTANCE);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature4Handler.INSTANCE.makeSensor(sensorId);
	}
}
