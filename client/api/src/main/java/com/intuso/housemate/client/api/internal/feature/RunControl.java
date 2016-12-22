package com.intuso.housemate.client.api.internal.feature;

import com.intuso.housemate.client.api.internal.annotation.*;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * API for running something
 */
@Feature
@Id(value = "run", name = "Run", description = "Run")
public interface RunControl {

    String ID = RunControl.class.getAnnotation(Id.class).value();

    /**
     * Start
     */
    @Command
    @Id(value = "start", name = "Start", description = "Start")
    void start();

    /**
     * Stop
     */
    @Command
    @Id(value = "stop", name = "Stop", description = "Stop")
    void stop();

    /**
     * API for running something with state
     */
    @Feature
    @Id(value = "run-stateful", name = "Run", description = "Run")
    interface Stateful extends RunControl {

        String ID = Stateful.class.getAnnotation(Id.class).value();

        /**
         * Get whether the device is running
         * @return true if the device is currently running
         */
        @Value("boolean")
        @Id(value = "running", name = "Running", description = "True if the device is currently running")
        boolean isRunning();

        /**
         * Add a listener
         */
        @AddListener
        ListenerRegistration addListener(Listener listener);
    }

    interface Listener {

        /**
         * Callback when running starts or stops
         * @param running true if the device is now running
         */
        @Value("boolean")
        @Id(value = "running", name = "Running", description = "True if the device is currently running")
        void running(boolean running);
    }
}
