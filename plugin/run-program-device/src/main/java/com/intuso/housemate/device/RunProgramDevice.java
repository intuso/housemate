package com.intuso.housemate.device;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Housemate device that will start and stop a program
 * @author tclabon
 *
 */
public class RunProgramDevice extends RealDevice {

	/**
	 * Thread to monitor whether the program is running
	 */
	private Monitor monitor = null;

	/**
	 * Property that describes the command to start the program
	 */
    private final RealProperty<String> command = StringType.createProperty(getResources(), "command", "Command", "The command for the program", "");

	/**
	 * The command to start the program
	 */
    private final RealCommand start = new RealCommand(getResources(), "start", "Start", "Start the program", new ArrayList<RealParameter<?>>()) {
		@Override
		public void perform(TypeInstances values) throws HousemateException {
			try {
				if(command.getTypeInstance().toString().length() == 0)
					throw new HousemateException("No command has been set");
				Runtime.getRuntime().exec(command.getTypeInstance().toString());
			} catch (Throwable t) {
				getLog().e("Could not start program: " + t.getMessage());
				getLog().st(t);
				throw new HousemateException("Could not start program", t);
			}
		}
	};

	/**
	 * The command to stop the program
	 */
	private final RealCommand stop = new RealCommand(getResources(), "stop", "Stop", "Stop the program", new ArrayList<RealParameter<?>>()) {
		@Override
		public void perform(TypeInstances values) throws HousemateException {
			Integer pid = getFirstPID();
			if(pid != null) {
				try {
					Runtime.getRuntime().exec("kill " + pid);
				} catch(IOException e) {
					throw new HousemateException("Failed to stop the program", e);
				}
			} else
				getLog().d("No program running, not stopping");
		}
	};

	/**
	 * The value for whether the program is running
	 */
    private final RealValue<Boolean> runningValue = BooleanType.createValue(getResources(), "running", "Running", "True if the program is currently running", false);

	/**
	 * Create a new start program device
	 * @param name the name of the device
	 * @throws HousemateException
	 */
	public RunProgramDevice(RealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
        getProperties().add(command);
        getCommands().add(start);
        getCommands().add(stop);
        getValues().add(runningValue);
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
					"ps -eopid,comm,user | grep -v grep | grep \"" + command.getTypeInstance() + "\" | cut -c1-5"
			};
			p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO - put device in error?
			getLog().e("Could not run check if program is running");
			getLog().st(e);
			return null;
		}

		int rc;
		try {
			rc = p.waitFor();
		} catch (InterruptedException e) {
			// TODO - put device in error?
			getLog().e("Interrupted waiting for check if program is running to complete");
			getLog().st(e);
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
						getLog().e("Could not parse PID");
						return null;
					}
				}
				return null;
			} catch(IOException e) {
				// TODO - put device in error?
				getLog().e("Could not read result of check if program is running");
				getLog().st(e);
				return null;
			}
		}
		
		try {
			getLog().e("Check if program running failed:");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line;
			while((line = in.readLine()) != null) {
				getLog().e(" - " + line);
			}
			return null;
		} catch(IOException e) {
			// TODO - put device in error?
			getLog().e("Check if program is running failed and could not read error");
			getLog().st(e);
			return null;
		}
	}

	@Override
	protected void start() {
		monitor = new Monitor();
		monitor.start();
	}

	@Override
	protected void stop() {
		// stop the monitor
		if(monitor != null)
			monitor.interrupt();
	}

	/**
	 * Thread that monitors whethe the program is running or not
	 * @author tclabon
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
					runningValue.setTypedValue(is_running);
				}

				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					getLog().d("Interrupted during loop sleep, stopping monitor thread");
					break;
				}
			}
		}
	}
}
