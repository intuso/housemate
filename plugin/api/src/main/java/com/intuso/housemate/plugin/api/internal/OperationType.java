package com.intuso.housemate.plugin.api.internal;

/**
 * Representation of a way of operating on values
 */
public interface OperationType {

    /**
     * Enumerator of simple operations
     */
    public enum Simple implements OperationType {

        Plus("plus", "Plus", "Adds the values together"),
        Minus("minus", "Minus", "Subtracts the second value fromthe first"),
        Times("times", "Times", "Times both values together"),
        Divide("divide", "Divide", "Divides the first number by the second"),
        Max("max", "Max", "Takes the maximum of the two values"),
        Min("min", "Min", "Takes the minimum of the two values");

        private final String id;
        private final String name;
        private final String description;

        private Simple(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    /**
     * Gets the id of the operator
     * @return the id of the operator
     */
    public String getId();

    /**
     * Gets the name of the operator
     * @return the name of the operator
     */
    public String getName();

    /**
     * Gets the description of the operator
     * @return the description of the operator
     */
    public String getDescription();
}
