package com.intuso.housemate.plugin.api.internal.feature;

import com.intuso.housemate.plugin.api.internal.annotations.Feature;
import com.intuso.housemate.plugin.api.internal.annotations.Id;
import com.intuso.housemate.plugin.api.internal.annotations.Value;
import com.intuso.housemate.plugin.api.internal.annotations.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@Id(value = "run-stateful", name = "Run", description = "Run")
public interface StatefulRunControl extends RunControl {

    @Values
    interface RunValues {

        /**
         * Callback to set the current power status of the device
         * @param isRunning true if the device is now on
         */
        @Value("boolean")
        @Id(value = "is-running", name = "Is Running", description = "True if the device is currently running")
        void isRunning(boolean isRunning);
    }
}
