package com.intuso.housemate.client.proxy.internal;

import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.view.CommandView;

/**
 * Classes implementing this are runnable objects
 * @param <START_STOP_COMMAND> the type of the command
 * @param <RUNNING_VALUE> the type of the value
 */
public interface ProxyRunnable<START_STOP_COMMAND extends Command<?, ?, ?, ?>, RUNNING_VALUE extends Value<?, ?, ?>> extends Runnable<START_STOP_COMMAND, RUNNING_VALUE> {
    void loadStartCommand(CommandView commandView);
    void loadStopCommand(CommandView commandView);
    boolean isRunning();
}