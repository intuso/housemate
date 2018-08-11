package com.intuso.housemate.client.api.internal.ability;

import com.intuso.housemate.client.api.internal.annotation.AddListener;
import com.intuso.housemate.client.api.internal.annotation.Command;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Value;
import com.intuso.utilities.collection.ManagedCollection;

/**
 * API for controlling power
 */
public interface Power {

    @Id(value = "power-control", name = "Power Control", description = "Power control")
    interface Control extends Ability {

        String ID = Control.class.getAnnotation(Id.class).value();

        /**
         * Turn on
         */
        @Command
        @Id(value = "on", name = "Turn On", description = "Turn on")
        void turnOn();

        /**
         * Turn off
         */
        @Command
        @Id(value = "off", name = "Turn Off", description = "Turn off")
        void turnOff();
    }

    @Id(value = "power-state", name = "Power State", description = "Power state")
    interface State extends Ability {

        String ID = State.class.getAnnotation(Id.class).value();

        /**
         * Add a listener
         */
        @AddListener
        ManagedCollection.Registration addListener(Listener listener);

        interface Listener {

            /**
             * Callback for when the device has been turned on or off
             *
             * @param on true if the device is now on, null if unknown
             */
            @Value
            @Id(value = "on", name = "On", description = "Whether the device is on")
            void on(Boolean on);
        }
    }

    @Id(value = "variable-power-control", name = "Variable Power Control", description = "Variable power control")
    interface VariableControl extends Ability {

        String ID = VariableControl.class.getAnnotation(Id.class).value();

        /**
         * Set the power
         */
        @Command
        @Id(value = "set", name = "Set", description = "Set Power")
        void percent(@Id(value = "percent", name = "Percent", description = "Percent") int volume);

        /**
         * Increase power
         */
        @Command
        @Id(value = "increase", name = "Increase", description = "Increase power")
        void increase();

        /**
         * Decrease power
         */
        @Command
        @Id(value = "decrease", name = "Decrease", description = "Decrease power")
        void decrease();

    }

    @Id(value = "variable-power-state", name = "Variable Power State", description = "Variable power state")
    interface VariableState extends Ability {

        String ID = VariableState.class.getAnnotation(Id.class).value();

        /**
         * Add a listener
         */
        @AddListener
        ManagedCollection.Registration addListener(Listener listener);

        interface Listener {

            /**
             * Callback for when the device power has been changed
             * @param percent the percent of power to the device, null if unknown
             */
            @Value
            @Id(value = "percent", name = "Percent", description = "Percent")
            void percent(Integer percent);
        }
    }
}