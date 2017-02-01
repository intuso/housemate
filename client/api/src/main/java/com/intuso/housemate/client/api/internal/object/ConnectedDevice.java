package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Renameable;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 * @param <DEVICE> the type of the feature
 */
public interface ConnectedDevice<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
        VALUES extends List<? extends Value<?, ?, ?>, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?, ?>, ?>,
        DEVICE extends ConnectedDevice<RENAME_COMMAND, COMMANDS, VALUES, PROPERTIES, DEVICE>>
        extends
        Object<ConnectedDevice.Listener<? super DEVICE>>,
        Renameable<RENAME_COMMAND>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES>,
        Property.Container<PROPERTIES> {

    String COMMANDS_ID = "command";
    String VALUES_ID = "value";
    String PROPERTIES_ID = "property";

    /**
     *
     * Listener interface for features
     */
    interface Listener<DEVICE extends ConnectedDevice<?, ?, ?, ?, ?>> extends Object.Listener,
            Renameable.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of features
     */
    interface Container<DEVICES extends List<? extends ConnectedDevice<?, ?, ?, ?, ?>, ?>> {

        /**
         * Gets the features list
         * @return the features list
         */
        DEVICES getConnectedDevices();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "feature";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
