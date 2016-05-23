package com.intuso.housemate.plugin.api.internal.feature;

import com.intuso.housemate.plugin.api.internal.annotations.Command;
import com.intuso.housemate.plugin.api.internal.annotations.Feature;
import com.intuso.housemate.plugin.api.internal.annotations.TypeInfo;

/**
 * Interface to mark real devices that provide power control
 */
@Feature
@TypeInfo(id = "run", name = "Run", description = "Run")
public interface RunControl {

    /**
     * Callback to turn the device on
     */
    @Command
    @TypeInfo(id = "start", name = "Start", description = "Start")
    void start();

    /**
     * Callback to turn the device off
     */
    @Command
    @TypeInfo(id = "stop", name = "Stop", description = "Stop")
    void stop();
}
