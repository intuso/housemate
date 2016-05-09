package com.intuso.housemate.client.api.internal.object;

/**
 * @param <DATA_TYPE> the type of the value
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public interface Property<
            DATA_TYPE,
            TYPE extends Type<?>,
            SET_COMMAND extends Command<?, ?, ?, ?>,
            PROPERTY extends Property<?, ?, ?, ?>>
        extends ValueBase<DATA_TYPE, TYPE, Property.Listener<? super PROPERTY>, PROPERTY> {

    String SET_COMMAND_ID = "set-command";
    String VALUE_PARAM = "value";

    /**
     * Sets the value of this property
     * @param value the new value
     * @param listener the listener to notify of progress
     */
    // todo use a PropertySet listener instead. Not every impl has a command to use in the PerformListener, eg RealPropertyBridge.
    void set(DATA_TYPE value, Command.PerformListener<? super SET_COMMAND> listener);

    /**
     * Gets the set value command
     * @return the set value command
     */
    SET_COMMAND getSetCommand();

    interface Listener<PROPERTY extends Property<?, ?, ?, ?>> extends ValueBase.Listener<PROPERTY> {}

    /**
     *
     * Interface to show that the implementing object has a list of properties
     */
    interface Container<PROPERTIES extends List<? extends Property<?, ?, ?, ?>, ?>> {

        /**
         * Gets the properties list
         * @return the properties list
         */
        PROPERTIES getProperties();
    }

    /**
     *
     * Data object for a property
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String TYPE = "property";

        private Data() {}

        public Data(String id, String name, String description) {
            super(TYPE, id, name, description);
        }
    }
}
