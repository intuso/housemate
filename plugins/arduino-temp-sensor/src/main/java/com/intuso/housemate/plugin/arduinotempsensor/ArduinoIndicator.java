package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.annotations.basic.Property;
import com.intuso.housemate.object.real.impl.device.StatefulPoweredDevice;
import com.intuso.utilities.log.Log;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 */
public class ArduinoIndicator extends StatefulPoweredDevice {

    private final SerialPort serialPort;

    @Property(id = "colour", name = "Colour", description = "Colour of the indicator", typeId = "string")
    public String colour;

    @Property(id = "intensity", name = "Intensity", description = "Intensity of the indicator", typeId = "integer")
    public int intensity;
    
    /**
     * Create a new device
     *
     * @param name the name of the device
     */
    protected ArduinoIndicator(Log log, SerialPort serialPort, String id, String name, String description) {
        super(log, id, name, description);
        this.serialPort = serialPort;
        getCustomPropertyIds().add("colour");
        getCustomPropertyIds().add("intensity");
    }

    @Override
    public void turnOn() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], (byte) ('0' + intensity)});
            setOn();
        } catch(SerialPortException e) {
            getLog().w("Failed to send command to turn light on");
        }
    }

    @Override
    public void turnOff() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
            setOff();
        } catch(SerialPortException e) {
            getLog().w("Failed to send command to turn light off");
        }
    }
}
