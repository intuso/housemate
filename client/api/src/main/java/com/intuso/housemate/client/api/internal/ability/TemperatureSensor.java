package com.intuso.housemate.client.api.internal.ability;

import com.intuso.housemate.client.api.internal.annotation.AddListener;
import com.intuso.housemate.client.api.internal.annotation.Command;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for temperature monitoring
 */
@Id(value = "temperaturesensor", name = "Temperature", description = "Temperature")
public interface TemperatureSensor extends Ability {

    String ID = TemperatureSensor.class.getAnnotation(Id.class).value();

    /**
     * Add a listener
     */
    @AddListener
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {

        /**
         * Callback for when the temperature of the device has changed
         * @param temperature the new temperature
         */
        @Value
        @Id(value = "temperature", name = "Temperature", description = "The current temperature, or null if unknown")
        void temperature(Double temperature);
    }

    @Id(value = "temperaturesensor.thermostat", name = "Thermostat", description = "Thermostat")
    interface Thermostat extends TemperatureSensor {

        /**
         * Set the target temperature
         */
        @Command
        @Id(value = "set", name = "Set", description = "Set target temperature")
        void set(@Id(value = "temperature", name = "Temperature", description = "Temperature") float temperature);
    }
}
