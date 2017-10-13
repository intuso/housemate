package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.ValueBaseView;

/**
 * @param <DATA_TYPE> the type of the value's type
 * @param <VALUE> the type of the value
 */
public interface ValueBase<DATA extends ValueBase.Data,
        DATA_TYPE,
        TYPE extends Type<?>,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VIEW extends ValueBaseView,
        VALUE extends ValueBase<?, ?, ?, ?, ?, ?>>
        extends Object<DATA, LISTENER, VIEW> {

    String VALUE_ID = "value";

    /**
     * Gets the value's type
     * @return the value's type
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
    interface Listener<VALUE extends ValueBase<?, ?, ?, ?, ?, ?>> extends Object.Listener {

        /**
         * Notifies that the value of this value object is about to be changed
         * @param value the value object whose value is about to be changed
         */
        void valueChanging(VALUE value);

        /**
         * Notifies that the value of this value object has been changed
         * @param value the value object whose value has just been changed
         */
        void valueChanged(VALUE value);
    }

    /**
     *
     * Data object for a value
     */
    class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        private String typePath;
        private int minValues;
        private int maxValues;

        public Data() {}

        public Data(String objectType, String id, String name, String description, String typePath, int minValues, int maxValues) {
            super(objectType, id, name,  description);
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

        public int getMaxValues() {
            return maxValues;
        }

        public void setMaxValues(int maxValues) {
            this.maxValues = maxValues;
        }

        public int getMinValues() {
            return minValues;
        }

        public void setMinValues(int minValues) {
            this.minValues = minValues;
        }
    }
}
