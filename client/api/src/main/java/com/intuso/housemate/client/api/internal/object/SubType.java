package com.intuso.housemate.client.api.internal.object;

/**
 * @param <SUB_TYPE> the type of the sub type's type
 */
public interface SubType<TYPE extends Type<?>,
            SUB_TYPE extends SubType<?, ?>>
        extends Object<SubType.Listener<? super SUB_TYPE>> {

    String TYPE_ID = "type";

    /**
     * Gets the sub types' type's id
     * @return the sub type's type's id
     */
    TYPE getType();

    /**
     *
     * Listener interface for sub types
     */
    interface Listener<SUB_TYPE extends SubType<?, ?>> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of sub types
     */
    interface Container<SUB_TYPES extends List<? extends SubType<?, ?>, ?>> {

        /**
         * Gets the sub type list
         * @return the sub type list
         */
        SUB_TYPES getSubTypes();
    }

    /**
     *
     * Data object for a sub type
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String TYPE = "subtype";

        public Data() {}

        public Data(String id, String name, String description) {
            super(TYPE, id, name, description);
        }
    }
}
