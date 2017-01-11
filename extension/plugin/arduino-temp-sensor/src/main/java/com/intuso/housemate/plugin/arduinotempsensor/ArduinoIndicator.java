package com.intuso.housemate.plugin.arduinotempsensor;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.PowerControl;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.io.IOException;

/**
 */
@Id(value = "arduino-indicator", name = "Arduino Indicator", description = "Arduino Indicator")
public class ArduinoIndicator implements FeatureDriver, PowerControl.Stateful {

    // todo use remote hardware

    private final Logger logger;
    private final Listeners<Listener> listeners;
    private final SerialPortWrapper serialPort;

    @Property
    @Id(value = "colour", name = "Colour", description = "Colour of the indicator")
    public String colour;

    @Property
    @Id(value = "intensity", name = "Intensity", description = "Intensity of the indicator")
    public int intensity;

    boolean on = true;

    @Inject
    protected ArduinoIndicator(@Assisted Logger logger,
                               @Assisted FeatureDriver.Callback driverCallback,
                               SerialPortWrapper serialPort,
                               ListenersFactory listenersFactory) {
        this.logger = logger;
        this.listeners = listenersFactory.create();
        this.serialPort = serialPort;
    }

    @Override
    public void startFeature() {}

    @Override
    public void stopFeature() {}

    @Override
    public void turnOn() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], (byte) ('0' + intensity)});
            on = true;
            for(Listener listener : listeners)
                listener.on(true);
        } catch(IOException e) {
            logger.warn("Failed to send command to turn light on");
        }
    }

    @Override
    public void turnOff() {
        try {
            serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
            on = false;
            for(Listener listener : listeners)
                listener.on(false);
        } catch(IOException e) {
            logger.warn("Failed to send command to turn light off");
        }
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public ListenerRegistration addListener(Listener listener) {
        return listeners.addListener(listener);
    }
}
