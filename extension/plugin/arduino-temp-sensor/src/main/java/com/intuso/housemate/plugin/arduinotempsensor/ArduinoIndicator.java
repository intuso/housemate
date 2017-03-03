package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.io.IOException;

/**
 */
@Id(value = "arduino-indicator", name = "Arduino Indicator", description = "Arduino Indicator")
public class ArduinoIndicator implements Power {

    private final Logger logger;
    private final ManagedCollection<Listener> listeners;
    private final SerialPortWrapper serialPort;

    @Property
    @Id(value = "colour", name = "Colour", description = "Colour of the indicator")
    public String colour;

    @Property
    @Id(value = "intensity", name = "Intensity", description = "Intensity of the indicator")
    public int intensity;

    Boolean on = null;

    protected ArduinoIndicator(Logger logger, ManagedCollectionFactory managedCollectionFactory, SerialPortWrapper serialPort) {
        this.logger = logger;
        this.serialPort = serialPort;
        this.listeners = managedCollectionFactory.create();
    }

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
    public ManagedCollection.Registration addListener(Listener listener) {
        listener.on(on);
        return listeners.add(listener);
    }
}
