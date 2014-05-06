package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;

/**
 * @param <I> The input type
 * @param <O> THe output type
 */
public interface Transformer<I, O> {

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
     * @param input the input value
     * @return the input value but of a different type
     * @throws com.intuso.housemate.api.HousemateException
     */
    public O apply(I input) throws HousemateException;
}
