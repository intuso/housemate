package com.intuso.housemate.client.api.internal.object;

/**
 * @param <DATA_TYPE> the type of the value's type
 * @param <VALUE> the type of the value
 */
public interface Value<DATA_TYPE,
            TYPE extends Type<?>,
            VALUE extends Value<?, ?, ?>>
        extends ValueBase<DATA_TYPE, TYPE, Value.Listener<? super VALUE>, VALUE> {

    /**
     * Gets the value's type's id
     * @return the value's type's id
     */
    TYPE getType();

    /**
     * Gets the value's value
     * @return the value's value
     */
    DATA_TYPE getValue();

    /**
     *
     * Listener interface for values
     */
    interface Listener<VALUE extends Value<?, ?, ?>> extends ValueBase.Listener<VALUE> {}

    /**
     *
     * Interface to show that the implementing object has a list of values
     */
    interface Container<VALUES extends List<? extends Value<?, ?, ?>, ?>> {

        /**
         * Gets the value list
         * @return the value list
         */
        VALUES getValues();
    }

    /**
     *
     * Data object for a value
     */
    final class Data extends ValueBase.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "value";

        public Data() {}

        public Data(String id, String name, String description, String type, int minValues, int maxValues) {
            super(OBJECT_TYPE, id, name,  description, type, minValues, maxValues);
        }
    }
}
