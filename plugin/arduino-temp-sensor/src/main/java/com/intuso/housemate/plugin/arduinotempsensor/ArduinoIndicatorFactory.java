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
    public ArduinoIndicator create(RealResources resources, String id, String name, String description) throws HousemateException {
        return new ArduinoIndicator(resources, serialPort, id, name, description);
    }
}
