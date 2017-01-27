package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for controlling volume
 */
@Feature
@Id(value = "volume", name = "Volume", description = "Volume")
public interface VolumeControl {

    String ID = VolumeControl.class.getAnnotation(Id.class).value();

    /**
     * Mute the device
     */
    @Command
    @Id(value = "mute", name = "Mute", description = "Mute")
    void mute();

    /**
     * Turn the volume up
     */
    @Command
    @Id(value = "volume-up", name = "Volume Up", description = "Volume up")
    void volumeUp();

    /**
     * Turn the volume down
     */
    @Command
    @Id(value = "volume-down", name = "Volume Down", description = "Volume down")
    void volumeDown();

    /**
     * API for controlling volume with state
     */
    @Feature
    @Id(value = "volume-stateful", name = "Volume", description = "Volume")
    interface Stateful extends VolumeControl {

        String ID = Stateful.class.getAnnotation(Id.class).value();

        /**
         * Get the volume
         * @return the current volume
         */
        @Value()
        @Id(value = "volume", name = "Current Volume", description = "The device's current volume")
        int getVolume();

        /**
         * Add a listener
         */
        @AddListener
        ManagedCollection.Registration addListener(Listener listener);
    }

    interface Listener {

        /**
         * Callback for when the volume has changed
         * @param volume the new volume
         */
        @Value()
        @Id(value = "volume", name = "Current Volume", description = "The device's current volume")
        void currentVolume(int volume);
    }
}
