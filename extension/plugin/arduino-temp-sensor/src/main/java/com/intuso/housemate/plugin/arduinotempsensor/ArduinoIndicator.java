package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.device.feature.RealStatefulPowerControl;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.utilities.log.Log;

import java.io.IOException;

/**
 */
@TypeInfo(id = "arduino-indicator", name = "Arduino Indicator", description = "Arduino Indicator")
public class ArduinoIndicator implements DeviceDriver, RealStatefulPowerControl {

    private final SerialPortWrapper serialPort;

    @Property("string")
    @TypeInfo(id = "colour", name = "Colour", description = "Colour of the indicator")
    public String colour;

    @Property("integer")
    @TypeInfo(id = "intensity", name = "Intensity", description = "Intensity of the indicator")
    public int intensity;

    private final Log log;

    @com.intuso.housemate.client.v1_0.real.api.annotations.Values
    protected Values values;

    @Inject
    protected ArduinoIndicator(Log log,
                               SerialPortWrapper serialPort,
                               @Assisted DeviceDriver.Callback driverCallback) {
        this.log = log;
        this.serialPort = serialPort;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void turnOn() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], (byte) ('0' + intensity)});
            values.isOn(true);
        } catch(IOException e) {
            log.w("Failed to send command to turn light on");
        }
    }

    @Override
    public void turnOff() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
            values.isOn(true);
        } catch(IOException e) {
            log.w("Failed to send command to turn light off");
        }
    }

    @Override
    public void setOn(boolean on) {
        values.isOn(on);
    }
}
