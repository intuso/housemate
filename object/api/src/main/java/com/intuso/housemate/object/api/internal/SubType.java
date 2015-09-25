package com.intuso.housemate.object.api.internal;

/**
 * @param <SUB_TYPE> the type of the sub type's type
 */
public interface SubType<SUB_TYPE extends SubType<?>> extends BaseHousemateObject<SubType.Listener<? super SUB_TYPE>> {

    /**
     * Gets the sub types' type's id
     * @return the sub type's type's id
     */
    String getTypeId();

    /**
     *
     * Listener interface for sub types
     */
    interface Listener<SUB_TYPE extends SubType<?>> extends ObjectListener {}

    /**
     *
     * Interface to show that the implementing object has a list of sub types
     */
    interface Container<SUB_TYPES extends List<? extends SubType<?>>> {

        /**
         * Gets the sub type list
         * @return the sub type list
         */
        SUB_TYPES getSubTypes();
    }
}
