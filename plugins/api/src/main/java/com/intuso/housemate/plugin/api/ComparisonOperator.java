package com.intuso.housemate.plugin.api;

/**
 * Representation of a way of comparing value
 */
public interface ComparisonOperator {

    /**
     * Enumerator of simple operations
     */
    public enum Simple implements ComparisonOperator {

        Equals("equal", "Equal", "True if two values are equal"),
        GreaterThan("greater-than", "Greater than", "True if the first value is greater than the second"),
        GreaterThanOrEqual("greater-than-or-equal", "Greater than or Equal to", "True if the first value is greater than or equal to the second"),
        LessThan("less-than", "Less Than", "True if the first value is less than the second"),
        LessThanOrEqual("less-than-or-equal", "Less Than or Equal to", "True if the first value is less than or equal to the second");

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
