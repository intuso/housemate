package com.intuso.housemate.api.object.application.instance;

import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <VALUE> the type of the value
 * @param <COMMAND> the type of the command
 * @param <APP_INSTANCE> the type of the application
 */
public interface ApplicationInstance<
            VALUE extends Value<?, ?>,
            COMMAND extends Command<?, ?, ?>,
            APP_INSTANCE extends ApplicationInstance<VALUE, COMMAND, APP_INSTANCE>>
        extends BaseHousemateObject<ApplicationInstanceListener<? super APP_INSTANCE>> {

    public final static String ALLOW_COMMAND_ID = "allow";
    public final static String REJECT_COMMAND_ID = "reject";
    public final static String STATUS_VALUE_ID = "status";

    public COMMAND getAllowCommand();
    public COMMAND getRejectCommand();
    public ApplicationInstanceStatus getStatus();
    public VALUE getStatusValue();
}
