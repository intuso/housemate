package com.intuso.housemate.client.real.api.internal.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@TypeInfo(id = "run-stateful", name = "Run", description = "Run")
public interface StatefulRunControl extends RunControl {

    @Values
    interface PowerValues {

        /**
         * Callback to set the current power status of the device
         * @param isRunning true if the device is now on
         */
        @Value("boolean")
        @TypeInfo(id = "is-running", name = "Is Running", description = "True if the device is currently running")
        void isRunning(boolean isRunning);
    }
}
