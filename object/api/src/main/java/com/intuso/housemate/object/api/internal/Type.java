package com.intuso.housemate.object.api.internal;

public interface Type<TYPE extends Type<?>> extends BaseHousemateObject<Type.Listener<? super TYPE>> {

    /**
     *
     * Listener interface for types
     */
    interface Listener<TYPE extends Type> extends ObjectListener {}
    /**
     *
     * Interface to show that the implementing object has a list of types
     */
    interface Container<TYPES extends List<? extends Type>> {

        /**
         * Gets the type list
         * @return the type list
         */
        TYPES getTypes();
    }
}
