package com.intuso.housemate.plugin.api.internal;

/**
 * @param <I> The input type
 * @param <O> THe output type
 */
public interface Transformer<I, O> {

    /**
     * Gets the type id for the input values
     * @return the input type of the transformation
     */
    public String getInputTypeId();

    /**
     * Gets the type id for the output value
     * @return the output type of the transformation
     */
    public String getOutputTypeId();

    /**
     * Applies the operation to the 2 values.
     *
     * @param input the input value
     * @return the input value but of a different type
     */
    public O apply(I input);
}
