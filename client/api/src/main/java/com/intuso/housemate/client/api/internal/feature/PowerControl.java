package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.AddListener;
import com.intuso.housemate.client.api.internal.annotation.Command;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for controlling power
 */
@Id(value = "power", name = "Power", description = "Power")
public interface PowerControl {

    String ID = PowerControl.class.getAnnotation(Id.class).value();

    /**
     * Turn on
     */
    @Command
    @Id(value = "on", name = "Turn On", description = "Turn the device on")
    void turnOn();

    /**
     * Turn off
     */
    @Command
    @Id(value = "off", name = "Turn Off", description = "Turn the device off")
    void turnOff();

    /**
     * Add a listener
     */
    @AddListener
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {

        /**
         * Callback for when the device has been turned on or off
         * @param on true if the device is now on, null if unknown
         */
        @Value
        @Id(value = "on", name = "On", description = "True if the device is now on")
        void on(Boolean on);
    }
}
