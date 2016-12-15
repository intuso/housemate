package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.annotations.Property;
import com.intuso.housemate.plugin.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.plugin.v1_0.api.feature.StatefulPowerControl;
import org.slf4j.Logger;

import java.io.IOException;

/**
 */
@Id(value = "arduino-indicator", name = "Arduino Indicator", description = "Arduino Indicator")
public class ArduinoIndicator implements FeatureDriver, StatefulPowerControl {

    // todo use remote hardware

    private final SerialPortWrapper serialPort;

    @Property("string")
    @Id(value = "colour", name = "Colour", description = "Colour of the indicator")
    public String colour;

    @Property("integer")
    @Id(value = "intensity", name = "Intensity", description = "Intensity of the indicator")
    public int intensity;

    private final Logger logger;

    protected PowerValues powerValues;

    @Inject
    protected ArduinoIndicator(SerialPortWrapper serialPort,
                               @Assisted Logger logger,
                               @Assisted FeatureDriver.Callback driverCallback) {
        this.logger = logger;
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
            powerValues.isOn(true);
        } catch(IOException e) {
            logger.warn("Failed to send command to turn light on");
        }
    }

    @Override
    public void turnOff() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
            powerValues.isOn(true);
        } catch(IOException e) {
            logger.warn("Failed to send command to turn light off");
        }
    }
}
