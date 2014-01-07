package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;

/**
 */
public class ArduinoIndicatorFactory implements RealDeviceFactory<ArduinoIndicator> {

    private final SerialPort serialPort;

    public ArduinoIndicatorFactory(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public String getTypeId() {
        return "arduino-indicator";
    }

    @Override
    public String getTypeName() {
        return "Arduino Indicator";
    }

    @Override
    public String getTypeDescription() {
        return "Arduino Indicator";
    }

    @Override
    public ArduinoIndicator create(Log log, String id, String name, String description) throws HousemateException {
        return new ArduinoIndicator(log, serialPort, id, name, description);
    }
}
