package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;

/**
 * @param <O> The type that is compared
 */
public interface Comparator<O> {

    /**
     * Gets the operator for the comparison
     * @return
     */
    public ComparisonOperator getOperator();

    /**
     * Gets the type id that the comparison is for
     * @return
     */
    public String getTypeId();

    /**
     * Compares two values.
     *
     * @param first the first value to compare
     * @param second the second value to compare
     * @return depends on the comparator used, but for eg GreateThan, returns true iff first > second
     * @throws HousemateException
     */
    public boolean compare(O first, O second) throws HousemateException;
}
