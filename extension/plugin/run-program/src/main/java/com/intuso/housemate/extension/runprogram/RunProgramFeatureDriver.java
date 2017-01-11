package com.intuso.housemate.extension.runprogram;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.FeatureDriver;
import com.intuso.housemate.client.v1_0.api.feature.RunControl;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Housemate feature that will start and stop a program
 *
 */
@Id(value = "run-program", name = "Run Program", description = "Run a program")
public class RunProgramFeatureDriver implements FeatureDriver, RunControl.Stateful {

    @Property
    @Id(value = "command", name = "Command", description = "The command for the program")
    private String command;

    private final Logger logger;
    private final Listeners<Listener> listeners;

    private Monitor monitor = null;
    private boolean isRunning = false;

    @Inject
    public RunProgramFeatureDriver(@Assisted Logger logger,
                                   @Assisted FeatureDriver.Callback driverCallback,
                                   ListenersFactory listenersFactory) {
        this.logger = logger;
        this.listeners = listenersFactory.create();
    }

    @Override
    public void startFeature() {
        monitor = new Monitor();
        monitor.start();
    }

    @Override
    public void stopFeature() {
        // stop the monitor
        if(monitor != null)
            monitor.interrupt();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public ListenerRegistration addListener(Listener listener) {
        return listeners.addListener(listener);
    }

    @Override
    public void start() {
        try {
            if(command ==  null || command.length() == 0)
                throw new FeatureException("No command has been set");
            Runtime.getRuntime().exec(command);
        } catch (Throwable t) {
            throw new FeatureException("Could not start program", t);
        }
    }

    @Override
    public void stop() {
        Integer pid = getFirstPID();
        if(pid != null) {
            try {
                Runtime.getRuntime().exec("kill " + pid);
            } catch(IOException e) {
                throw new FeatureException("Failed to stop the program", e);
            }
        } else
            throw new FeatureException("No program running, not stopping");
    }

    /**
     * Get the first PID of a process matching the command property
     * @return the first PID of a process matching the command or null if no process exists
     */
    private Integer getFirstPID() {
        Process p;
        try {
            String[] cmd = {
                    "/bin/sh",
                    "-c",
                    "ps -eopid,comm,user | grep -v grep | grep \"" + command + "\" | cut -c1-5"
            };
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            // TODO - put feature in error?
            logger.error("Could not run check if program is running", e);
            return null;
        }

        int rc;
        try {
            rc = p.waitFor();
        } catch (InterruptedException e) {
            // TODO - put feature in error?
            logger.error("Interrupted waiting for check if program is running to complete", e);
            return null;
        }

        if(rc == 0) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = in.readLine();
                if(line != null) {
                    try {
                        return new Integer(line.trim());
                    } catch(NumberFormatException e) {
                        // TODO - put feature in error?
                        logger.error("Could not parse PID");
                        return null;
                    }
                }
                return null;
            } catch(IOException e) {
                // TODO - put feature in error?
                logger.error("Could not read result of check if program is running", e);
                return null;
            }
        }

        try {
            logger.error("Check if program running failed:");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while((line = in.readLine()) != null) {
                logger.error(" - " + line);
            }
            return null;
        } catch(IOException e) {
            // TODO - put feature in error?
            logger.error("Check if program is running failed and could not read error", e);
            return null;
        }
    }

    /**
     * Thread that monitors whethe the program is running or not
     * @author Tom Clabon
     *
     */
    private class Monitor extends Thread {

        @Override
        public void run() {
            boolean is_running = true;
            while(!isInterrupted()) {
                is_running = getFirstPID() != null;
                if(is_running != isRunning) {
                    isRunning = is_running;
                    for(Listener listener : listeners)
                        listener.running(isRunning);
                }

                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    logger.debug("Interrupted during loop sleep, stopping monitor thread");
                    break;
                }
            }
        }
    }
}
