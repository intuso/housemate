package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;
import org.slf4j.Logger;

/**
 * Created by tomc on 02/12/15.
 */
public class Temperature3Handler extends TemperatureHandler {

    static Temperature3Handler INSTANCE;

    public Temperature3Handler(Logger logger, RFXtrx rfxtrx) {
        super(logger, TemperatureSensors.forTemp3(rfxtrx));
        INSTANCE = this;
    }

    @Override
    public String getNewDeviceName(int sensorId) {
        return "Temperature 3 " + sensorId;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-temperature-3";
    }
}