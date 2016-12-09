package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;

;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 * @param <FEATURE> the type of the feature
 */
public interface Feature<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        START_STOP_COMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?, ?>,
        ERROR_VALUE extends Value<?, ?, ?>,
        DRIVER_PROPERTY extends Property<?, ?, ?, ?>,
        DRIVER_LOADED_VALUE extends Value<?, ?, ?>,
        COMMANDS extends List<? extends Command<?, ?, ?, ?>, ?>,
        VALUES extends List<? extends Value<?, ?, ?>, ?>,
        PROPERTIES extends List<? extends Property<?, ?, ?, ?>, ?>,
        FEATURE extends Feature<RENAME_COMMAND, REMOVE_COMMAND, START_STOP_COMMAND, RUNNING_VALUE, ERROR_VALUE, DRIVER_PROPERTY, DRIVER_LOADED_VALUE, COMMANDS, VALUES, PROPERTIES, FEATURE>>
        extends
        Object<Feature.Listener<? super FEATURE>>,
        Renameable<RENAME_COMMAND>,
        Runnable<START_STOP_COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<REMOVE_COMMAND>,
        UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES>,
        Property.Container<PROPERTIES> {

    String COMMANDS_ID = "commands";
    String VALUES_ID = "values";
    String PROPERTIES_ID = "properties";

    /**
     *
     * Listener interface for features
     */
    interface Listener<FEATURE extends Feature<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends Object.Listener,
            UsesDriver.Listener<FEATURE>,
            Failable.Listener<FEATURE>,
            Renameable.Listener<FEATURE>,
            Runnable.Listener<FEATURE> {}

    /**
     *
     * Interface to show that the implementing object has a list of features
     */
    interface Container<FEATURES extends List<? extends Feature<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>> {

        /**
         * Gets the features list
         * @return the features list
         */
        FEATURES getFeatures();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "feature";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }
}
