package com.intuso.housemate.object.api.internal;

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
        RUNNING_VALUE extends Value<?, ?>,
        ERROR_VALUE extends Value<?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?>>,
        FEATURES extends List<? extends Feature<?, ?, ?>>,
        DEVICE extends Device<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, PROPERTIES, FEATURES, DEVICE>>
        extends
        BaseHousemateObject<Device.Listener<? super DEVICE>>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE>,
        Property.Container<PROPERTIES>,
        Feature.Container<FEATURES> {

    /**
     *
     * Listener interface for devices
     */
    interface Listener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends ObjectListener,
            Failable.Listener<DEVICE>,
            Renameable.Listener<DEVICE>,
            Runnable.Listener<DEVICE>,
            UsesDriver.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the devices list
         * @return the devices list
         */
        DEVICES getDevices();
    }
}
