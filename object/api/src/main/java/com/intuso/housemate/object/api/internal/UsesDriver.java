package com.intuso.housemate.object.api.internal;

/**
 * Classes implementing this are runnable objects
 * @param <DRIVER_PROPERTY> the type of the id property
 * @param <DRIVER_LOADED_VALUE> the type of the loaded value
 */
public interface UsesDriver<DRIVER_PROPERTY extends Property<?, ?, ?>, DRIVER_LOADED_VALUE> {

    /**
     * Gets the running value object
     * @return the running value object
     */
    DRIVER_PROPERTY getDriverProperty();

    /**
     * Gets the running value object
     * @return the running value object
     */
    DRIVER_LOADED_VALUE getDriverLoadedValue();

    interface Listener<USES_DRIVER extends UsesDriver> {

        /**
         * Notifies that the primary object's running status has changed
         * @param usesDriver the object whose driver was (un)loaded
         * @param loaded whether the driver is loaded or not
         */
        void driverLoaded(USES_DRIVER usesDriver, boolean loaded);
    }
}