package com.intuso.housemate.plugin.api.internal;

/**
 * @param <I> The input types
 * @param <O> THe output type
 */
public interface Operator<I, O> {

    /**
     * Gets the type of the operation
     * @return the type of the operation
     */
    public OperationType getOperationType();

    /**
     * Gets the type id for the input values
     * @return the type id of the input values
     */
    public String getInputTypeId();

    /**
     * Gets the type id for the output value
     * @return the type id of the output value
     */
    public String getOutputTypeId();

    /**
     * Applies the operation to the 2 values.
     *
     * @param first the first value to compare
     * @param second the second value to compare
     * @return depends on the operator used, but for eg Plus, returns first + second
     */
    public O apply(I first, I second);
}
