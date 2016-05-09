package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;

/**
 * @param <RENAME_COMMAND> the type of the command for renaming the device
 * @param <REMOVE_COMMAND> the type of the command for removing the device
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <DEVICE> the type of the device
 */
public interface Device<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?, ?>,
        ERROR_VALUE extends Value<?, ?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?, ?>, ?>,
        FEATURES extends List<? extends Feature<?, ?, ?>, ?>,
        DEVICE extends Device<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, PROPERTIES, FEATURES, DEVICE>>
        extends
        Object<Device.Listener<? super DEVICE>>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE>,
        Property.Container<PROPERTIES>,
        Feature.Container<FEATURES> {

    String PROPERTIES_ID = "properties";
    String FEATURES_ID = "features";

    /**
     *
     * Listener interface for devices
     */
    interface Listener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends Object.Listener,
            Failable.Listener<DEVICE>,
            Renameable.Listener<DEVICE>,
            Runnable.Listener<DEVICE>,
            UsesDriver.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>> {

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

        public final static String TYPE = "device";

        public Data() {}

        public Data(String id, String name, String description) {
            super(TYPE, id, name, description);
        }
    }
}
