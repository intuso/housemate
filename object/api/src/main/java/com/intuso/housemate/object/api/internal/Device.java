package com.intuso.housemate.object.api.internal;

/**
 * @param <RENAME_COMMAND> the type of the command for renaming the device
 * @param <REMOVE_COMMAND> the type of the command for removing the device
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <COMMAND> the type of the commands
 * @param <COMMANDS> the type of the commands list
 * @param <VALUE> the type of the values
 * @param <VALUES> the type of the values list
 * @param <PROPERTY> the type of the properties
 * @param <PROPERTIES> the type of the properties list
 * @param <DEVICE> the type of the device
 */
public interface Device<
        RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        COMMAND extends Command<?, ?, ?, ?>,
        COMMANDS extends List<? extends COMMAND>,
        RUNNING_VALUE extends Value<?, ?>,
        ERROR_VALUE extends Value<?, ?>,
        VALUE extends Value<?, ?>,
        VALUES extends List<? extends VALUE>,
        PROPERTY extends Property<?, ?, ?>,
        PROPERTIES extends List<? extends PROPERTY>,
        DEVICE extends Device<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, COMMAND, COMMANDS, RUNNING_VALUE, ERROR_VALUE, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE>>
        extends
        BaseHousemateObject<Device.Listener<? super DEVICE>>,
        Renameable<RENAME_COMMAND>,
        com.intuso.housemate.object.api.internal.Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES>,
        Property.Container<PROPERTIES> {

    /**
     * Get the ids of all the device's features
     * @return the ids of all the device's features
     */
    java.util.List<String> getFeatureIds();

    /**
     * Get the ids of all the commands that do not belong to features
     * @return the ids of all the commands that do not belong to features
     */
    java.util.List<String> getCustomCommandIds();

    /**
     * Get the ids of all the values that do not belong to features
     * @return the ids of all the values that do not belong to features
     */
    java.util.List<String> getCustomValueIds();

    /**
     * Get the ids of all the properties that do not belong to features
     * @return the ids of all the properties that do not belong to features
     */
    java.util.List<String> getCustomPropertyIds();

    /**
     *
     * Listener interface for devices
     */
    interface Listener<DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
            extends ObjectListener,
            Failable.Listener<DEVICE>,
            Renameable.Listener<DEVICE>,
            com.intuso.housemate.object.api.internal.Runnable.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

        /**
         * Gets the devices list
         * @return the devices list
         */
        DEVICES getDevices();
    }

    /**
     * Base interface for all device features
     */
    interface Feature {}
}
