package com.intuso.housemate.client.real.api.internal.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Command;
import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.Id;

/**
 * Interface to mark real devices that provide power control
 */
@Feature
@Id(value = "run", name = "Run", description = "Run")
public interface RunControl {

    /**
     * Callback to turn the device on
     */
    @Command
    @Id(value = "start", name = "Start", description = "Start")
    void start();

    /**
     * Callback to turn the device off
     */
    @Command
    @Id(value = "stop", name = "Stop", description = "Stop")
    void stop();
}
