package com.intuso.housemate.object.api.internal;

/**
 * @param <SUB_TYPES> the type of the sub types list
 */
public interface Option<SUB_TYPES extends List<? extends SubType<?>>,
        OPTION extends Option<?, ?>>
        extends BaseHousemateObject<Option.Listener<? super OPTION>>, SubType.Container<SUB_TYPES> {

    /**
     *
     * Listener interface for options
     */
    interface Listener<OPTION extends Option<?, ?>> extends ObjectListener {}

    /**
     *
     * Interface to show that the implementing object has a list of options
     */
    interface Container<OPTIONS extends List<? extends Option<?, ?>>> {

        /**
         * Gets the option list
         * @return the option list
         */
        OPTIONS getOptions();
    }
}
