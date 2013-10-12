package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

public interface RunnableObject<START_STOP_COMMAND extends Command<?, ?>, RUNNING_VALUE extends Value<?, ?>> {

    /**
     * Gets the start command
     * @return the start command
     */
    public START_STOP_COMMAND getStartCommand();

    /**
     * Gets the stop command
     * @return the stop command
     */
    public START_STOP_COMMAND getStopCommand();

    /**
     * Gets the running value object
     * @return the running value object
     */
    public RUNNING_VALUE getRunningValue();

    /**
     * Gets the running value
     * @return the running value
     */
    public boolean isRunning();
}