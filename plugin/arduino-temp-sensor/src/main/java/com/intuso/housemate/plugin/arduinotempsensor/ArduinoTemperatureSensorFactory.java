package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import jssc.SerialPort;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/06/13
 * Time: 13:13
 * To change this template use File | Settings | File Templates.
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
    public ArduinoTemperatureSensor create(RealResources resources, String id, String name, String description) throws HousemateException {
        return new ArduinoTemperatureSensor(resources, serialPort, id, name, description);
    }
}
