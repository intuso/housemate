package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "temperature-1", name = "Temperature1", description = "Temperature1 Sensor")
public class Temperature1Sensor extends TemperatureSensor {

	@Inject
	public Temperature1Sensor(@Assisted RealDevice device) {
		super(device);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return RFXtrx433Hardware.INSTANCE.makeTemperature1(sensorId);
	}
}
