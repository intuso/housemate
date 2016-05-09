package com.intuso.housemate.client.api.internal.object;

/**
 * @param <PARAMETER> the type of the subclass
 */
public interface Parameter<TYPE extends Type<?>,
            PARAMETER extends Parameter<TYPE, PARAMETER>>
        extends Object<Parameter.Listener<? super PARAMETER>> {

    String TYPE_ID = "type";

    /**
     * Gets the type id of the parameter
     * @return the type id of the parameter
     */
    TYPE getType();

    /**
     *
     * Listener interface for parameters
     */
    interface Listener<PARAMETER extends Parameter<?, ?>> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of parameters
     */
    interface Container<PARAMETERS extends List<? extends Parameter<?, ?>, ?>> {

        /**
         * Gets the parameters list
         * @return the parameters list
         */
        PARAMETERS getParameters();
    }

    /**
     * Data object for a parameter
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String TYPE = "parameter";

        public Data() {}

        public Data(String id, String name, String description) {
            super(TYPE, id, name, description);
        }
    }
}
