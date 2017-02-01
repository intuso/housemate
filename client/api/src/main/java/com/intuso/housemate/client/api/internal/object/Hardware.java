package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the device
 */
public interface Hardware<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?, ?>,
        ERROR_VALUE extends Value<?, ?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?, ?>,
        COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
        VALUES extends List<? extends Value<?, ?, ?>, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?, ?>, ?>,
        DEVICES extends List<? extends ConnectedDevice<?, ?, ?, ?, ?>, ?>,
        HARDWARE extends Hardware<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, COMMANDS, VALUES, PROPERTIES, DEVICES, HARDWARE>>
        extends
        Object<Hardware.Listener<? super HARDWARE>>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES>,
        Property.Container<PROPERTIES>,
        ConnectedDevice.Container<DEVICES> {

    String COMMANDS_ID = "command";
    String VALUES_ID = "value";
    String PROPERTIES_ID = "property";
    String DEVICES_ID = "device";

    /**
     *
     * Listener interface for devices
     */
    interface Listener<HARDWARE extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends Object.Listener,
            Failable.Listener<HARDWARE>,
            Renameable.Listener<HARDWARE>,
            Runnable.Listener<HARDWARE>,
            UsesDriver.Listener<HARDWARE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<HARDWARES extends List<? extends Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>> {

        /**
         * Gets the hardware list
         * @return the hardware list
         */
        HARDWARES getHardwares();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "hardware";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
