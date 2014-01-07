package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;

/**
 */
public class ArduinoTemperatureSensorFactory implements RealDeviceFactory<ArduinoTemperatureSensor> {

    private final SerialPort serialPort;

    public ArduinoTemperatureSensorFactory(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public String getTypeId() {
        return "arduino-temp-sensor";
    }

    @Override
    public String getTypeName() {
        return "Arduino Temperature Sensor";
    }

    @Override
    public String getTypeDescription() {
        return "Arduino Temperature Sensor";
    }

    @Override
    public ArduinoTemperatureSensor create(Log log, String id, String name, String description) throws HousemateException {
        return new ArduinoTemperatureSensor(log, serialPort, id, name, description);
    }
}
