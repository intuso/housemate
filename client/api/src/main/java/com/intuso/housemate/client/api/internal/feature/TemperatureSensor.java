package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.AddListener;
import com.intuso.housemate.client.api.internal.annotation.Feature;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for temperature monitoring
 */
@Feature
@Id(value = "temperature", name = "Temperature", description = "Temperature")
public interface TemperatureSensor {

    String ID = TemperatureSensor.class.getAnnotation(Id.class).value();

    /**
     * Get the temperature of the device
     * @return the temperature
     */
    @Value
    @Id(value = "temperature", name = "Temperature", description = "The current temperature")
    double getTemperature();

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
        @Id(value = "temperature", name = "Temperature", description = "The current temperature")
        void temperature(double temperature);
    }
}
