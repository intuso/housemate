package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.impl.device.StatefulPoweredDevice;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.log.Log;

import java.io.IOException;

/**
 */
@TypeInfo(id = "arduino-indicator", name = "Arduino Indicator", description = "Arduino Indicator")
public class ArduinoIndicator extends StatefulPoweredDevice {

    private final SerialPortWrapper serialPort;

    @Property(id = "colour", name = "Colour", description = "Colour of the indicator", typeId = "string")
    public String colour;

    @Property(id = "intensity", name = "Intensity", description = "Intensity of the indicator", typeId = "integer")
    public int intensity;

    private final Log log;

    @Inject
    protected ArduinoIndicator(Log log,
                               SerialPortWrapper serialPort,
                               @Assisted RealDevice device) {
        this.log = log;
        this.serialPort = serialPort;
    }

    @Override
    public void turnOn() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], (byte) ('0' + intensity)});
            setOn();
        } catch(IOException e) {
            log.w("Failed to send command to turn light on");
        }
    }

    @Override
    public void turnOff() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
            setOff();
        } catch(IOException e) {
            log.w("Failed to send command to turn light off");
        }
    }
}
