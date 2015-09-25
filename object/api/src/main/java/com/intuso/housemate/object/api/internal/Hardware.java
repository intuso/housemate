package com.intuso.housemate.object.api.internal;

/**
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the device
 */
public interface Hardware<
            PROPERTIES extends List<? extends Property<?, ?, ?>>,
            HARDWARE extends Hardware<PROPERTIES, HARDWARE>>
        extends
            BaseHousemateObject<Hardware.Listener<? super HARDWARE>>, Property.Container<PROPERTIES> {

    /**
     *
     * Listener interface for devices
     */
    interface Listener<HARDWARE extends Hardware<?, ?>> extends ObjectListener {
        void hardwareConnected(HARDWARE hardware, boolean connected);
    }

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<HARDWARES extends List<? extends Hardware<?, ?>>> {

        /**
         * Gets the hardware list
         * @return the hardware list
         */
        HARDWARES getHardwares();
    }
}
