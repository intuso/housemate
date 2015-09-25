package com.intuso.housemate.object.api.internal;

/**
 * @param <PARAMETER> the type of the subclass
 */
public interface Parameter<PARAMETER extends Parameter<PARAMETER>>
        extends BaseHousemateObject<Parameter.Listener<? super PARAMETER>> {

    /**
     * Gets the type id of the parameter
     * @return the type id of the parameter
     */
    String getTypeId();

    /**
     *
     * Listener interface for parameters
     */
    interface Listener<PARAMETER extends Parameter<?>> extends ObjectListener {}

    /**
     *
     * Interface to show that the implementing object has a list of parameters
     */
    interface Container<PARAMETERS extends List<? extends Parameter<?>>> {

        /**
         * Gets the parameters list
         * @return the parameters list
         */
        PARAMETERS getParameters();
    }
}
