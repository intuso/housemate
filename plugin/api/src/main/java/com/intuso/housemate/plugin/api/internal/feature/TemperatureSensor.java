package com.intuso.housemate.plugin.api.internal.feature;

import com.intuso.housemate.plugin.api.internal.annotations.Feature;
import com.intuso.housemate.plugin.api.internal.annotations.Id;
import com.intuso.housemate.plugin.api.internal.annotations.Value;
import com.intuso.housemate.plugin.api.internal.annotations.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@Id(value = "temperature", name = "Temperature", description = "Temperature")
public interface TemperatureSensor {

    @Values
    interface TemperatureValues {

        /**
         * Callback to set the current temperature of the device
         * @param temperature the current temperature
         */
        @Value("double")
        @Id(value = "temperature", name = "Temperature", description = "The current temperature")
        void setTemperature(double temperature);
    }
}
