package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;

/**
 * @param <COMMAND> the type of the command
 * @param <ERROR_VALUE> the type of the error value
 * @param <SYSTEM> the type of the device
 */
public interface System<
        ERROR_VALUE extends Value<?, ?, ?>,
        COMMAND extends Command<?, ?, ?, ?>,
        DEVICES extends List<? extends Property<?, ?, ?, ?>, ?>,
        SYSTEM extends System<ERROR_VALUE, COMMAND, DEVICES, SYSTEM>>
        extends
        Object<System.Listener<? super SYSTEM>>,
        Renameable<COMMAND>,
        Failable<ERROR_VALUE>,
        Removeable<COMMAND> {

    String PLAYBACK = "playback";
    String ADD_PLAYBACK = "add-playback";
    String POWER = "power";
    String ADD_POWER = "add-power";
    String RUN = "run";
    String ADD_RUN = "add-run";
    String TEMPERATURE_SENSOR = "temperature-sensor";
    String ADD_TEMPERATURE_SENSOR = "add-temperature-sensor";
    String VOLUME = "volume";
    String ADD_VOLUME = "add-volume";

    DEVICES getPlaybackDevices();
    COMMAND getAddPlaybackDeviceCommand();
    DEVICES getPowerDevices();
    COMMAND getAddPowerDeviceCommand();
    DEVICES getRunDevices();
    COMMAND getAddTemperatureSensorDeviceCommand();
    DEVICES getTemperatureSensorDevices();
    COMMAND getAddRunDeviceCommand();
    DEVICES getVolumeDevices();
    COMMAND getAddVolumeDeviceCommand();

    /**
     *
     * Listener interface for devices
     */
    interface Listener<SYSTEM extends System<?, ?, ?, ?>> extends Object.Listener,
            Failable.Listener<SYSTEM>,
            Renameable.Listener<SYSTEM> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<SYSTEMS extends List<? extends System<?, ?, ?, ?>, ?>> {

        /**
         * Gets the devices list
         * @return the devices list
         */
        SYSTEMS getSYSTEMS();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "system";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
