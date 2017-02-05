package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.Runnable;

/**
 * @param <COMMAND> the type of the command
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <DEVICE> the type of the device
 */
public interface Device<
        ERROR_VALUE extends Value<?, ?, ?>,
        COMMAND extends Command<?, ?, ?, ?>,
        DEVICES extends List<? extends Property<?, ?, ?, ?>, ?>,
        DEVICE extends Device<ERROR_VALUE, COMMAND, DEVICES, DEVICE>>
        extends
        Object<Device.Listener<? super DEVICE>>,
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
    interface Listener<DEVICE extends Device<?, ?, ?, ?>> extends Object.Listener,
            Failable.Listener<DEVICE>,
            Renameable.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<DEVICES extends List<? extends Device<?, ?, ?, ?>, ?>> {

        /**
         * Gets the devices list
         * @return the devices list
         */
        DEVICES getDevices();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "device";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
