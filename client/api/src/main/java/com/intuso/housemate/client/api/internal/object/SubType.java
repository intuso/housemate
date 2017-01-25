package com.intuso.housemate.client.api.internal.object;

/**
 * @param <SUB_TYPE> the type of the sub type's type
 */
public interface SubType<TYPE extends Type<?>,
            SUB_TYPE extends SubType<?, ?>>
        extends Object<SubType.Listener<? super SUB_TYPE>> {

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

        public final static String OBJECT_CLASS = "subtype";

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
