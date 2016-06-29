package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;
import org.slf4j.Logger;

/**
 * Created by tomc on 02/12/15.
 */
public class Temperature5Handler extends TemperatureHandler {

    static Temperature5Handler INSTANCE;

    public Temperature5Handler(Logger logger, RFXtrx rfxtrx, RealDevice.Container deviceContainer) {
        super(logger, TemperatureSensors.forTemp5(rfxtrx), deviceContainer);
        INSTANCE = this;
    }

    @Override
    public String getNewDeviceName(int sensorId) {
        return "Temperature 5 " + sensorId;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-temperature-5";
    }
}