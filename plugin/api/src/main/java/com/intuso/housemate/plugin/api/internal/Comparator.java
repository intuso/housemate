package com.intuso.housemate.plugin.api.internal;

/**
 * @param <O> The type that is compared
 */
public interface Comparator<O> {

    /**
     * Gets the operator for the comparison
     * @return the operator for the comparison
     */
    public ComparisonType getComparisonType();

    /**
     * Gets the type id that the comparison is performed on
     * @return the type that the comparison is performed on
     */
    public String getTypeId();

    /**
     * Compares two values.
     *
     * @param first the first value to compare
     * @param second the second value to compare
     * @return depends on the comparator used, but for eg GreaterThan, returns true iff first &gt; second
     */
    public boolean compare(O first, O second);
}
