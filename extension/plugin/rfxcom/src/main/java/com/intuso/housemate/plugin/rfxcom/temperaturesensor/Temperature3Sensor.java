package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.housemate.plugin.rfxcom.RFXtrx433Hardware;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Housemate device that controls a USB relay
 *
 */
@TypeInfo(id = "temperature-3", name = "Temperature3", description = "Temperature3 Sensor")
public class Temperature3Sensor extends TemperatureSensor {

	@Inject
	public Temperature3Sensor(Log log,
                              ListenersFactory listenersFactory,
                              @Assisted DeviceData data) {
		super(log, listenersFactory, "temperature-3", data);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return RFXtrx433Hardware.INSTANCE.makeTemperature3(sensorId);
	}
}
