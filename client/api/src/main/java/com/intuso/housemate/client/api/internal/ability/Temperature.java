package com.intuso.housemate.client.api.internal.ability;

import com.intuso.housemate.client.api.internal.annotation.AddListener;
import com.intuso.housemate.client.api.internal.annotation.Command;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for temperature monitoring
 */
public interface Temperature {

    @Id(value = "temperature-control", name = "Temperature Control", description = "Temperature control")
    interface Control extends Ability {

        String ID = Control.class.getAnnotation(Id.class).value();

        /**
         * Set the target temperature
         */
        @Command
        @Id(value = "set", name = "Set", description = "Set target temperature")
        void set(@Id(value = "temperature", name = "Temperature", description = "Temperature") float temperature);
    }

    @Id(value = "temperature-state", name = "Temperature State", description = "Temperature state")
    interface State extends Ability {

        String ID = State.class.getAnnotation(Id.class).value();

        /**
         * Add a listener
         */
        @AddListener
        ManagedCollection.Registration addListener(Listener listener);

        interface Listener {

            /**
             * Callback for when the temperature of the device has changed
             *
             * @param temperature the new temperature
             */
            @Value
            @Id(value = "temperature", name = "Temperature", description = "The current temperature, or null if unknown")
            void temperature(Double temperature);
        }
    }
}
