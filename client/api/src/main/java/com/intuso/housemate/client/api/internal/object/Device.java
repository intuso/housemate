package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.Runnable;

/**
 * @param <COMMAND> the type of the command
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <DEVICE> the type of the device
 */
public interface Device<
        COMMAND extends Command<?, ?, ?, ?>,
        RUNNING_VALUE extends Value<?, ?, ?>,
        ERROR_VALUE extends Value<?, ?, ?>,
        FEATURES extends List<? extends Feature<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        DEVICE extends Device<COMMAND, RUNNING_VALUE, ERROR_VALUE, FEATURES, DEVICE>>
        extends
        Object<Device.Listener<? super DEVICE>>,
        Renameable<COMMAND>,
        Runnable<COMMAND, RUNNING_VALUE>,
        Failable<ERROR_VALUE>,
        Removeable<COMMAND>,
        Feature.Container<FEATURES> {

    String FEATURES_ID = "feature";
    String ADD_FEATURE_ID = "add-feature";

    COMMAND getAddFeatureCommand();

    /**
     *
     * Listener interface for devices
     */
    interface Listener<DEVICE extends Device<?, ?, ?, ?, ?>> extends Object.Listener,
            Failable.Listener<DEVICE>,
            Renameable.Listener<DEVICE>,
            Runnable.Listener<DEVICE> {}

    /**
     *
     * Interface to show that the implementing object has a list of devices
     */
    interface Container<DEVICES extends List<? extends Device<?, ?, ?, ?, ?>, ?>> {

        /**
         * Gets the devices list
         * @return the devices list
         */
        DEVICES getDevices();
    }

    /**
     * Data object for a device
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_TYPE = "device";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_TYPE, id, name, description);
        }
    }
}
