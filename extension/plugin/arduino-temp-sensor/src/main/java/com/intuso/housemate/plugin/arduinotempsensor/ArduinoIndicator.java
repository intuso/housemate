package com.intuso.housemate.plugin.arduinotempsensor;

import com.intuso.housemate.client.v1_0.api.annotation.Classes;
import com.intuso.housemate.client.v1_0.api.annotation.Component;
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
public class ArduinoIndicator {

    private final Logger logger;
    private final SerialPortWrapper serialPort;

    @Property
    @Id(value = "colour", name = "Colour", description = "Colour of the indicator")
    public String colour;

    @Property
    @Id(value = "intensity", name = "Intensity", description = "Intensity of the indicator")
    public int intensity;

    @Component
    @Id(value = "power", name = "Power", description = "Power")
    public final PowerComponent powerComponent;

    protected ArduinoIndicator(Logger logger, ManagedCollectionFactory managedCollectionFactory, SerialPortWrapper serialPort) {
        this.logger = logger;
        this.serialPort = serialPort;
        this.powerComponent = new PowerComponent(managedCollectionFactory);
    }

    @Classes(Classes.LIGHT)
    private final class PowerComponent implements Power.Control, Power.State {

        private final ManagedCollection<Listener> listeners;

        Boolean on = null;

        private PowerComponent(ManagedCollectionFactory managedCollectionFactory) {
            this.listeners = managedCollectionFactory.createSet();
        }

        private synchronized void setOn(boolean on) {
            this.on = on;
            for (Listener listener : listeners)
                listener.on(on);
        }

        @Override
        public synchronized void turnOn() {
            try {
                serialPort.writeBytes(new byte[]{colour.getBytes()[0], (byte) ('0' + intensity)});
                setOn(true);
            } catch (IOException e) {
                logger.warn("Failed to send command to turn light on");
            }
        }

        @Override
        public synchronized void turnOff() {
            try {
                serialPort.writeBytes(new byte[]{colour.getBytes()[0], '0'});
                setOn(false);
            } catch (IOException e) {
                logger.warn("Failed to send command to turn light off");
            }
        }

        @Override
        public synchronized ManagedCollection.Registration addListener(Listener listener) {
            listener.on(on);
            return listeners.add(listener);
        }
    }
}
