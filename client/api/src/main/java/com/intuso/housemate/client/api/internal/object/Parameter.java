package com.intuso.housemate.client.api.internal.object;

/**
 * @param <PARAMETER> the type of the subclass
 */
public interface Parameter<TYPE extends Type<?>,
            PARAMETER extends Parameter<TYPE, PARAMETER>>
        extends Object<Parameter.Data, Parameter.Listener<? super PARAMETER>> {

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
    interface Container<PARAMETERS extends Iterable<? extends Parameter<?, ?>>> {

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

        public final static String OBJECT_CLASS = "parameter";

        private String typePath;
        private int minValues;
        private int maxValues;

        public Data() {}

        public Data(String id, String name, String description, String typePath, int minValues, int maxValues) {
            super(OBJECT_CLASS, id, name, description);
            this.typePath = typePath;
            this.minValues = minValues;
            this.maxValues = maxValues;
        }

        public String getTypePath() {
            return typePath;
        }

        public void setTypePath(String typePath) {
            this.typePath = typePath;
        }

        public int getMinValues() {
            return minValues;
        }

        public void setMinValues(int minValues) {
            this.minValues = minValues;
        }

        public int getMaxValues() {
            return maxValues;
        }

        public void setMaxValues(int maxValues) {
            this.maxValues = maxValues;
        }
    }
}
