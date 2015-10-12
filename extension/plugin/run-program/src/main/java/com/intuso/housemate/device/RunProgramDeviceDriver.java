package com.intuso.housemate.device;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.annotations.Command;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.Values;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.plugin.v1_0.api.TypeInfo;
import com.intuso.utilities.log.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Housemate device that will start and stop a program
 *
 */
@TypeInfo(id = "run-program", name = "Run Program", description = "Run a program")
public class RunProgramDeviceDriver implements DeviceDriver {

    @Property(id = "command", name = "Command", description = "The command for the program", typeId = "string")
    private String command;

    @Values
    public MyValues values;

    private final Log log;
    private Monitor monitor = null;

    @Inject
    public RunProgramDeviceDriver(Log log,
                                  @Assisted RealDevice device) {
        this.log = log;
    }

    @Command(id = "start", name = "Start", description = "Start the program")
    public void startProgram() {
        try {
            if(command ==  null || command.length() == 0)
                throw new HousemateCommsException("No command has been set");
            Runtime.getRuntime().exec(command);
        } catch (Throwable t) {
            throw new HousemateCommsException("Could not start program", t);
        }
    }

    @Command(id = "stop", name = "Stop", description = "Stop the program")
    public void stopProgram() {
        Integer pid = getFirstPID();
        if(pid != null) {
            try {
                Runtime.getRuntime().exec("kill " + pid);
            } catch(IOException e) {
                throw new HousemateCommsException("Failed to stop the program", e);
            }
        } else
            throw new HousemateCommsException("No program running, not stopping");
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
			// TODO - put device in error?
			log.e("Could not run check if program is running", e);
			return null;
		}

		int rc;
		try {
			rc = p.waitFor();
		} catch (InterruptedException e) {
			// TODO - put device in error?
			log.e("Interrupted waiting for check if program is running to complete", e);
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
						// TODO - put device in error?
						log.e("Could not parse PID");
						return null;
					}
				}
				return null;
			} catch(IOException e) {
				// TODO - put device in error?
				log.e("Could not read result of check if program is running", e);
				return null;
			}
		}
		
		try {
			log.e("Check if program running failed:");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line;
			while((line = in.readLine()) != null) {
				log.e(" - " + line);
			}
			return null;
		} catch(IOException e) {
			// TODO - put device in error?
			log.e("Check if program is running failed and could not read error", e);
			return null;
		}
	}

	@Override
	public void start() {
		monitor = new Monitor();
		monitor.start();
	}

	@Override
	public void stop() {
		// stop the monitor
		if(monitor != null)
			monitor.interrupt();
	}

    public interface MyValues {
        @Value(id = "running", name = "Running", description = "True if the program is currently running", typeId = "integer")
        void setRunning(boolean running);
    }

	/**
	 * Thread that monitors whethe the program is running or not
	 * @author Tom Clabon
	 *
	 */
	private class Monitor extends Thread {

		/**
		 * True if the program us running
		 */
		private boolean isRunning = false;

		@Override
		public void run() {
			boolean is_running = true;
			while(!isInterrupted()) {
				is_running = getFirstPID() != null;
				if(is_running != isRunning) {
					isRunning = is_running;
					values.setRunning(is_running);
				}

				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					log.d("Interrupted during loop sleep, stopping monitor thread");
					break;
				}
			}
		}
	}
}
