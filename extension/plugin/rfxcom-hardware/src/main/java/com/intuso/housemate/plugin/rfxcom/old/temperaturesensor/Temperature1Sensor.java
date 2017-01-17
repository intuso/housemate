package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-temperature-1", name = "Temperature1", description = "Temperature1 Sensor")
public class Temperature1Sensor extends TemperatureSensor {

	@Inject
	public Temperature1Sensor() {
		super(Temperature1Handler.INSTANCE);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature1Handler.INSTANCE.makeSensor(sensorId);
	}
}
