package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;

/**
 * @param <I> The input types
 * @param <O> THe output type
 */
public interface Operator<I, O> {

    /**
     * Gets the operator for the comparison
     * @return
     */
    public OperationType getOperationType();

    /**
     * Gets the type id for the input values
     * @return
     */
    public String getInputTypeId();

    /**
     * Gets the type id for the output value
     * @return
     */
    public String getOutputTypeId();

    /**
     * Applies the operation to the 2 values.
     *
     * @param first the first value to compare
     * @param second the second value to compare
     * @return depends on the operator used, but for eg Plus, returns first + second
     * @throws com.intuso.housemate.api.HousemateException
     */
    public O apply(I first, I second) throws HousemateException;
}
