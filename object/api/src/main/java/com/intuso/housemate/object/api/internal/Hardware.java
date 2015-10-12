package com.intuso.housemate.object.api.internal;

/**
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the device
 */
public interface Hardware<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?>,
        ERROR_VALUE extends Value<?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?>>,
        HARDWARE extends Hardware<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, PROPERTIES, HARDWARE>>
        extends
        BaseHousemateObject<Hardware.Listener<? super HARDWARE>>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE>,
        Property.Container<PROPERTIES> {

    /**
     *
     * Listener interface for devices
     */
    interface Listener<HARDWARE extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>> extends ObjectListener,
            Failable.Listener<HARDWARE>,
            Renameable.Listener<HARDWARE>,
            Runnable.Listener<HARDWARE>,
            UsesDriver.Listener<HARDWARE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<HARDWARES extends List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the hardware list
         * @return the hardware list
         */
        HARDWARES getHardwares();
    }
}
