package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;

/**
 * Created by tomc on 02/12/15.
 */
public class Temperature1Handler extends TemperatureHandler {

    static Temperature1Handler INSTANCE;

    public Temperature1Handler(Log log, RFXtrx rfxtrx, RealDevice.Container deviceContainer) {
        super(log, TemperatureSensors.forTemp1(rfxtrx), deviceContainer);
        INSTANCE = this;
    }

    @Override
    public String getNewDeviceName(int sensorId) {
        return "Temperature 1 " + sensorId;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-temperature-1";
    }
}
