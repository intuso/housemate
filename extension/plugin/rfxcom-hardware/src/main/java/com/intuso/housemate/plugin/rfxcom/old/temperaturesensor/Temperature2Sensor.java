package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.annotation.Id;

/**
 * Housemate device that controls a USB relay
 *
 */
@Id(value = "rfxcom-temperature-2", name = "Temperature2", description = "Temperature2 Sensor")
public class Temperature2Sensor extends TemperatureSensor {

	@Inject
	public Temperature2Sensor() {
		super(Temperature2Handler.INSTANCE);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return Temperature2Handler.INSTANCE.makeSensor(sensorId);
	}
}
