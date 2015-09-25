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
@TypeInfo(id = "temperature-2", name = "Temperature2", description = "Temperature2 Sensor")
public class Temperature2Sensor extends TemperatureSensor {

	@Inject
	public Temperature2Sensor(Log log,
                              ListenersFactory listenersFactory,
                              @Assisted DeviceData data) {
		super(log, listenersFactory, "temperature-2", data);
	}

    public com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensor createSensor(int sensorId) {
        return RFXtrx433Hardware.INSTANCE.makeTemperature2(sensorId);
	}
}
