package com.intuso.housemate.object.api.internal;

/**
 * @param <DATA_TYPE> the type of the value
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public interface Property<
            DATA_TYPE,
            SET_COMMAND extends Command<?, ?, ?, ?>,
            PROPERTY extends Property<DATA_TYPE, SET_COMMAND, PROPERTY>>
        extends ValueBase<DATA_TYPE, Property.Listener<? super PROPERTY>, PROPERTY> {

    /**
     * Sets the value of this property
     * @param value the new value
     * @param listener the listener to notify of progress
     */
    void set(DATA_TYPE value, Command.PerformListener<? super SET_COMMAND> listener);

    /**
     * Gets the set value command
     * @return the set value command
     */
    SET_COMMAND getSetCommand();

    interface Listener<PROPERTY extends Property<?, ?, ?>> extends ValueBase.Listener<PROPERTY> {}

    /**
     *
     * Interface to show that the implementing object has a list of properties
     */
    interface Container<PROPERTIES extends List<? extends Property<?, ?, ?>>> {

        /**
         * Gets the properties list
         * @return the properties list
         */
        PROPERTIES getProperties();
    }
}
